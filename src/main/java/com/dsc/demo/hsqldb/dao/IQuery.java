package com.dsc.demo.hsqldb.dao;

public interface IQuery {

	static final String COUNT = "SELECT COUNT(1) FROM PUBLIC.TEST_TABLE";
	
	static final String INSERT = "INSERT INTO PUBLIC.TEST_TABLE (TRACK_ID, TITLE, MIX, IS_REMIXED, RELEASE_DATE, GENRE_ID, SUBGENRE_ID, TRACK_URL, BPM, DURATION, DURATION_MS, ISRC, KEY_ID, LABEL_ID, RELEASE_ID, UPDATED_ON, IS_MATCHED_SPOT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
}
