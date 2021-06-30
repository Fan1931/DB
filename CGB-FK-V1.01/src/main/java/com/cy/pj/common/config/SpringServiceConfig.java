package com.cy.pj.common.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class SpringServiceConfig {

	  @Bean
	  public DataSourceTransactionManager newTxManager(DataSource dataSource) {
		  DataSourceTransactionManager tx=new DataSourceTransactionManager();
		  tx.setDataSource(dataSource);
		  return tx;
	  }

}
