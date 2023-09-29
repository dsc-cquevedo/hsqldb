package com.dsc.demo.hsqldb.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDAO implements IBaseDAO {
		
	protected JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public long getCount() throws DataAccessException {
		return this.jdbcTemplate.queryForObject(IQuery.COUNT, Long.class);
	}

	@Override
	public boolean insert(List<List<String>> rows) throws DataAccessException {
		final int[] affected = this.jdbcTemplate.batchUpdate(IQuery.INSERT, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				final List<String> row = rows.get(i);
				int j = 1;
				for (final String data : row) {
					ps.setString(j++, data);
				}
			}

			@Override
			public int getBatchSize() {
				return rows.size();
			}

		});

		return affected.length == rows.size();
	}
	
}
