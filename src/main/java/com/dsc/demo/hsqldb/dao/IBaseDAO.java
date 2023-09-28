package com.dsc.demo.hsqldb.dao;

import org.springframework.dao.DataAccessException;

public interface IBaseDAO {

	long getCount() throws DataAccessException;

}
