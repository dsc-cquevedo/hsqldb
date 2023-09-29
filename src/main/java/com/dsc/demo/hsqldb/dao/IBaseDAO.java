package com.dsc.demo.hsqldb.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface IBaseDAO {

	long getCount() throws DataAccessException;
	
	boolean insert(List<List<String>> rows) throws DataAccessException;

}
