package com.epam.esm.config;

import com.epam.esm.datasource.CustomDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.epam.esm")
@PropertySource("classpath:application.properties")
public class DataConfig {
    @Value("${pool.capacity}")
    private int poolCapacity;

    @Value("${pool.timeout.seconds}")
    private int timeoutSeconds;

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    @Profile("!test")
    public CustomDataSource defaultDataSource() {
        CustomDataSource customDataSource = CustomDataSource.getInstance();
        customDataSource.setTimeoutSeconds(timeoutSeconds);
        customDataSource.setPoolCapacity(poolCapacity);
        customDataSource.setDriverClassName(driverClassName);
        customDataSource.setUrl(dbUrl);
        customDataSource.setUsername(dbUsername);
        customDataSource.setPassword(dbPassword);
        return customDataSource;
    }

    @Bean
    @Profile("test")
    public DataSource embeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
