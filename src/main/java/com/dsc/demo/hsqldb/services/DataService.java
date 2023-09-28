package com.dsc.demo.hsqldb.services;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.text.MessageFormat;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	public void count(String dbms) throws Exception {
		LOGGER.info(MessageFormat.format("DBMS: {0} ", dbms));
		final LocalTime init = LocalTime.now();
		
		LOGGER.info(MessageFormat.format("Count {0}: {1,number,#}", dbms, (dbms.equals("H2") ? this.h2DAO : this.hsqldbDAO).getCount()));
		
		final LocalTime end = LocalTime.now();
		LOGGER.info(MessageFormat.format("Duration Total {0} seconds", MILLIS.between(init, end)/(double)1000));
	}

}
