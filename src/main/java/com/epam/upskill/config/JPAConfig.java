package com.epam.upskill.config;


import org.hibernate.SessionFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Value("${hibernate.dialect}")
  private String hibernateDialect;
  @Value("${hibernate.hbm2ddl.auto}")
  private String hbm2ddlAuto;

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
    entityManagerFactory.setDataSource(dataSource);
    entityManagerFactory.setPackagesToScan("com.epam.upskill.entity");
    entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    entityManagerFactory.setJpaProperties(hibernateProperties());
    entityManagerFactory.setPersistenceUnitName("yourPersistenceUnitName");
    return entityManagerFactory;
  }

  private Properties hibernateProperties() {
    var properties = new Properties();
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
    properties.setProperty("hibernate.format_sql", hibernateDialect);
    properties.setProperty("hibernate.default_schema", "gym_schema");
    return properties;
  }

  @Bean
  public DataSource dataSource() {
    var dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    return dataSource;
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }
}






