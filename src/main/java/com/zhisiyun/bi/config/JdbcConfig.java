package com.zhisiyun.bi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcConfig {
	@Bean(name = "dataSource")
	@Qualifier("dataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "dsDataSource")
	@Qualifier("dsDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.ds")
	public DataSource biDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "dsJdbcTemplate")
	public JdbcTemplate dsJdbcTemplate(@Qualifier("dsDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean(name = "dataDataSource")
	@Qualifier("dataDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.data")
	public DataSource dataDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "dataJdbcTemplate")
	public JdbcTemplate dataJdbcTemplate(@Qualifier("dataDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
