package com.hungle.spring.config;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
public class MariaDb4jConfig {
    private static final String DB_SERVICE = "dbServiceBean";

    @Value("${db.port:3306}")
    private Integer port;

    @Bean(name = { DB_SERVICE })
    public MariaDB4jSpringService mariaDb() {
        MariaDB4jSpringService mariaDb = new MariaDB4jSpringService();
        mariaDb.setDefaultPort(port);
        return mariaDb;
    }

    @Bean
    @DependsOn(DB_SERVICE)
    public DataSource dataSource(DataSourceProperties dataSourceProperties) throws ClassNotFoundException {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(getDriverClassByName(dataSourceProperties.determineDriverClassName()));
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        return dataSource;
    }

    @SuppressWarnings("unchecked")
    private Class<Driver> getDriverClassByName(String className) {
        try {
            return (Class<Driver>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }
}
