//package com.epam.upskill.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.init.DataSourceInitializer;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Slf4j
//@Configuration
//@Profile("test")
//@EnableTransactionManagement
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@PropertySource("classpath:application-test.yml")
//public class H2Config {
//
//  @Value("${spring.jpa.properties.hibernate.dialect}")
//  private String hibernateDialect;
//
//  @Value("${spring.jpa.hibernate.ddl-auto}")
//  private String hbm2ddlAuto;
//
//  @Value("${spring.datasource.url}")
//  private String h2Url;
//
//  @Value("${spring.datasource.username}")
//  private String h2Username;
//
//  @Value("${spring.datasource.password}")
//  private String h2Password;
//
//  @Value("${spring.datasource.driver-class-name}")
//  private String h2DriverClassName;
//
//
//  @Bean
//  public LocalContainerEntityManagerFactoryBean entityManagerFactoryH2(DataSource dataSource) {
//    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//    entityManagerFactory.setDataSource(dataSource);
//    entityManagerFactory.setPackagesToScan("com.epam.upskill.entity");
//    entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//    entityManagerFactory.setJpaProperties(hibernatePropertiesH2());
//    entityManagerFactory.setPersistenceUnitName("yourPersistenceUnitName");
//    return entityManagerFactory;
//  }
//
//  @Bean
//  public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//    return new PersistenceExceptionTranslationPostProcessor();
//  }
//
//  private Properties hibernatePropertiesH2() {
//    Properties properties = new Properties();
//    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//    properties.setProperty("hibernate.hbm2ddl.auto", "update");
//    properties.setProperty("hibernate.format_sql", "true");
//    log.info("Используются hibernatePropertiesH2: {}", properties);
//    return properties;
//  }
//
//
//  @Bean
//  public DataSource h2DataSource() {
//    var dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(h2DriverClassName);
//    dataSource.setUrl(h2Url);
//    dataSource.setUsername(h2Username);
//    dataSource.setPassword(h2Password);
//    return dataSource;
//  }
//  @Bean
//  public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
//    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
//    resourceDatabasePopulator.addScript(new ClassPathResource("data.sql"));
//
//    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
//    dataSourceInitializer.setDataSource(dataSource);
//    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
//
//    return dataSourceInitializer;
//  }
//
//}
//server:
//  port: 8092
//
//spring:
//  config:
//    activate:
//      on-profile: test
//  h2:
//    console:
//     enabled: true
//     path: /h2-console
//  datasource:
//    driver-class-name: org.h2.Driver
//    url: jdbc:h2:mem:testdb
//    username: sa
//    password:
//    initialization-mode: always
//    data: classpath:schema.sql
//  jpa:
//    hibernate:
//      ddl-auto: create-drop
//      show-sql: true
//    show-sql: true
//    properties:
//      hibernate:
//        dialect: org.hibernate.dialect.H2Dialect
//
//logging:
//  level:
//    org:
//      hibernate:
//        sql: DEBUG
