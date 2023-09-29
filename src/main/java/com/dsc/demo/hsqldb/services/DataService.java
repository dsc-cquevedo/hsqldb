package com.dsc.demo.hsqldb.services;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dsc.demo.hsqldb.dao.H2DAO;
import com.dsc.demo.hsqldb.dao.HsqldbDAO;

@Service
public class DataService {
	
	private static final Logger LOGGER = LogManager.getLogger(DataService.class);
	
	@Autowired
	private H2DAO h2DAO;
	@Autowired
	private HsqldbDAO hsqldbDAO;
	
	@Value("${application.files.path}")
	private String filesPath;
	
	public void count(String dbms) {
		LOGGER.info(MessageFormat.format("DBMS: {0} ", dbms));
		final LocalTime init = LocalTime.now();
		
		LOGGER.info(MessageFormat.format("Count {0}: {1,number,#}", dbms, (dbms.equals("H2") ? this.h2DAO : this.hsqldbDAO).getCount()));
		
		final LocalTime end = LocalTime.now();
		LOGGER.info(MessageFormat.format("Duration Total {0} seconds", MILLIS.between(init, end)/(double)1000));
	}
	
	public void insert(String dbms) {
		LOGGER.info(MessageFormat.format("DBMS: {0} ", dbms));
		final LocalTime init = LocalTime.now();
		
		final Path path = Paths.get(MessageFormat.format("{0}/{1}", this.filesPath, "bp_track.csv"));
		try (final BufferedReader reader = Files.newBufferedReader(path)){
			final CSVParser parser = CSVParser.parse(reader, CSVFormat.Builder.create().setDelimiter(',').build());
			final Iterator<CSVRecord> iterator = parser.iterator();
			final List<List<String>> rows = new ArrayList<>();
			boolean header = true;
			final int commitSize = 10000;
			while (iterator.hasNext()) {
				final CSVRecord csvRecord = iterator.next();
				if ( !header ) {
					final List<String> row = new ArrayList<>();
					for (int i = 0; i < csvRecord.size(); i++) {
						row.add(csvRecord.get(i).trim());
					}
					rows.add(row);
					
					if ( rows.size() == commitSize ) {
						this.sendToPersist(dbms, rows);
					}
				}
				header = false;
			}
			
			if ( rows.size() > 0 ) {
				this.sendToPersist(dbms, rows);
			}
		} catch (final IOException e) {
			LOGGER.error(e);
		}
		
		final LocalTime end = LocalTime.now();
		LOGGER.info(MessageFormat.format("Duration Total {0} seconds", MILLIS.between(init, end)/(double)1000));
	}

	private void sendToPersist(String dbms, List<List<String>> rows) {
		(dbms.equals("H2") ? this.h2DAO : this.hsqldbDAO).insert(rows);
		rows.clear();
	}

}
