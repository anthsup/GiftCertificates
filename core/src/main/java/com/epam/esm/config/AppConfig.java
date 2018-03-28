package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

// TODO probably gonna be refactored into smaller ones later
@Configuration
public class AppConfig {

    // PostgreSQL info goes here
    // TODO change to custom connection pool later
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("");
        ds.setUrl("");
        ds.setUsername("");
        ds.setPassword("");
        return ds;
    }
}
