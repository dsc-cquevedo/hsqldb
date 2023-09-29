package com.dsc.demo.hsqldb.services;

import java.text.MessageFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ResourcesServices {

	private static final Logger LOGGER = LogManager.getLogger(ResourcesServices.class);
	
	@Scheduled(cron = "0 * * * * *")
	public void reportCurrentTime() {
		final Runtime runtime = Runtime.getRuntime();
		
		final double dividen = 1024 * 1024; //MB
		
		LOGGER.info(MessageFormat.format("Memory | Total {0,number,#.##} MB | Free {1,number,#.##} MB | Used {2,number,#.##} MB | Max {3,number,#.##} MB", runtime.totalMemory() / dividen, runtime.freeMemory() / dividen, (runtime.totalMemory() - runtime.freeMemory()) / dividen, runtime.maxMemory() / dividen));
	}
	
}
