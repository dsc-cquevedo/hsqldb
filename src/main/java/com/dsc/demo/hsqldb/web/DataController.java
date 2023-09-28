package com.dsc.demo.hsqldb.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dsc.demo.hsqldb.services.DataService;


@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

private static final Logger LOGGER = LogManager.getLogger(DataController.class);
	
	@Autowired
	private DataService service;
	
	@GetMapping(path = "/count/{dbms}")
	public ResponseEntity<Object> count(@PathVariable("dbms") String dbms) {
		final Object response = null;
		HttpStatus status = HttpStatus.OK;
		
		try {
			this.service.count(dbms);
		} catch (final Exception e) {
			LOGGER.info(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return ResponseEntity.status(status).body(response);
	}
	
}
