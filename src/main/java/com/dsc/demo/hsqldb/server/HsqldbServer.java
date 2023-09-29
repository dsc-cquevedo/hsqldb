package com.dsc.demo.hsqldb.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hsqldb.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.dsc.demo.hsqldb.dao.HsqldbDAO;

@Component
public class HsqldbServer {
	
	private final static Logger LOGGER = LogManager.getLogger(HsqldbServer.class);
	
	@Autowired
	private HsqldbDAO dao;
	
	@Value("${application.datasource.hsqldb.url}")
	private String url;
	@Value("${application.datasource.hsqldb.username}")
	private String username;
	@Value("${application.datasource.hsqldb.password}")
	private String password;
	@Value("${application.datasource.hsqldb.port}")
	private String port;
	@Value("${application.datasource.hsqldb.driverClassName}")
	private String driver;
	@Value("${application.datasource.hsqldb.path}")
	private String path;
	private String completePath;
	
	private Server hsqlServer;

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

		LOGGER.info("Starting HSQLDB server...");
		this.hsqlServer = new Server();
		this.hsqlServer.setLogWriter(null);
        this.hsqlServer.setSilent(true);
        this.hsqlServer.setDatabaseName(0, "ans");
        this.hsqlServer.setDatabasePath(0, MessageFormat.format("file:{0}/ans.db;user={1};password={2}", this.completePath, this.username, this.password));
        this.hsqlServer.start();
		
        this.setDataSource();
        
		LOGGER.info("HSQLDB server started");
		
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
		LOGGER.info("Ending HSQLDB server...");
		this.hsqlServer.stop();
		LOGGER.info("HSQLDB server ended");
	}
	
}
