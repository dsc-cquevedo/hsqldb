package com.dsc.demo.hsqldb.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.dsc.demo.hsqldb.dao.H2DAO;

@Component
public class H2Server {
	
	private final static Logger LOGGER = LogManager.getLogger(H2Server.class);
	
	@Autowired
	private H2DAO dao;
	
	@Value("${application.datasource.h2.url}")
	private String url;
	@Value("${application.datasource.h2.username}")
	private String username;
	@Value("${application.datasource.h2.password}")
	private String password;
	@Value("${application.datasource.h2.port}")
	private String port;
	@Value("${application.datasource.h2.driverClassName}")
	private String driver;
	@Value("${application.datasource.h2.path}")
	private String path;
	private String completePath;

	@PostConstruct
	private void startServer() {
		this.completePath = FilenameUtils.separatorsToUnix(MessageFormat.format("{0}/{1}", System.getProperty("user.dir"), this.path));
		final Path completePathFile = Paths.get(this.completePath);
		if ( !Files.exists(completePathFile) ) {
			try {
				Files.createDirectories(completePathFile);
			} catch (final IOException e) {
				LOGGER.error(e);
			}
		}
		
		try {
			LOGGER.info("Starting H2 server...");
			Server.createTcpServer("-tcpPort", this.port, "-tcpPassword", this.password, "-baseDir", this.completePath, "-tcpAllowOthers").start();
			
			Class.forName(this.driver);
			final Connection con = DriverManager.getConnection(MessageFormat.format("jdbc:h2:{0}/rep", this.completePath), this.username, this.password);
			con.close();
			
			this.setDataSource();
			
			LOGGER.info("H2 server started");
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.error(e);
		}
	}
	
	private void setDataSource() {
		final DataSource dataSource = DataSourceBuilder
			.create()
			.username(this.username)
			.password(this.password)
			.url(this.url)
			.driverClassName(this.driver)
			.build();
		this.dao.setDataSource(dataSource);
	}

	@PreDestroy
	private void endServer() {
		try {
			LOGGER.info("Ending H2 server...");
			Server.shutdownTcpServer("tcp://localhost:" + this.port, this.password, true, true);
			LOGGER.info("H2 server ended");
		} catch (final SQLException e) {
			LOGGER.error(e);
		}
	}
	
}
