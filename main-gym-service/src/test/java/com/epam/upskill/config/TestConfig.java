//package com.epam.upskill.config;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Slf4j
//@Configuration
//@Profile("test")
//@EnableTransactionManagement
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@PropertySource("classpath:application-test.yml")
//public class TestConfig {
//
//  @Value("${spring.datasource.url}")
//  private String url;
//
//  @Value("${spring.datasource.username}")
//  private String username;
//
//  @Value("${spring.datasource.password}")
//  private String password;
//
//  @Value("${spring.datasource.driver-class-name}")
//  private String driverClassName;
//
//  @Value("${hibernate.dialect}")
//  private String hibernateDialect;
//  @Value("${hibernate.hbm2ddl.auto}")
//  private String hbm2ddlAuto;
//
//  @Bean
//  public LocalContainerEntityManagerFactoryBean entityManagerFactoryTest(DataSource dataSource) {
//    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//    entityManagerFactory.setDataSource(dataSource);
//    entityManagerFactory.setPackagesToScan("upskill.entity");
//    entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//    entityManagerFactory.setJpaProperties(hibernatePropertiesTest());
//    entityManagerFactory.setPersistenceUnitName("Test Name");
//    return entityManagerFactory;
//  }
//
//  private Properties hibernatePropertiesTest() {
//    var properties = new Properties();
//    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//    properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
//    properties.setProperty("hibernate.format_sql", hibernateDialect);
//    properties.setProperty("my_custom_schema", "test_gym_schema");
//    properties.setProperty("hibernate.cache.use_second_level_cache", "false");
//    log.info(" TestConfig: {}", properties);
//    return properties;
//  }
//
//  @Bean
//  public DataSource dataSourceTest() {
//    var dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(driverClassName);
//    dataSource.setUrl(url);
//    dataSource.setUsername(username);
//    dataSource.setPassword(password);
//    return dataSource;
//  }
//
//  @Bean
//  public PlatformTransactionManager transactionManagerTest(EntityManagerFactory entityManagerFactory) {
//    JpaTransactionManager transactionManager = new JpaTransactionManager();
//    transactionManager.setEntityManagerFactory(entityManagerFactory);
//    return transactionManager;
//  }
//}
