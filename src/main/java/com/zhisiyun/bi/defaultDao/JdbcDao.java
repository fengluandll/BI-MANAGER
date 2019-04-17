package com.zhisiyun.bi.defaultDao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class JdbcDao {

	@Autowired
	@Qualifier("dataJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * jdbc查询
	 * 
	 */
	public List<Map<String, Object>> query(String sql, Map params) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		return namedParameterJdbcTemplate.queryForList(sql, params);
	}

	/***
	 * count 查询
	 * 
	 * ***/
	public Integer queryCount(String sql) {
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

}
