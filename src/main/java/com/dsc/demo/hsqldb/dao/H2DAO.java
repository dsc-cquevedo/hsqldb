package com.dsc.demo.hsqldb.dao;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class H2DAO implements IBaseDAO {
		
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public long getCount() throws DataAccessException {
		return this.jdbcTemplate.queryForObject(IQuery.COUNT, Long.class);
	}

}
