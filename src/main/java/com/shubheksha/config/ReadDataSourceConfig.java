package com.shubheksha.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
		"com.shubheksha.read.repository" }, entityManagerFactoryRef = "readEntityManager", transactionManagerRef = "readTransactionManager")
public class ReadDataSourceConfig {
	public static final Logger logger = LoggerFactory.getLogger(ReadDataSourceConfig.class);

	@Value("${spring.shubheksha.read.db.properties}")
	private String dbProps;

	@Bean(name = "readDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.read.datasource")
	DataSource readDataSource() {
		logger.info("configuring read datasource");
		try {
			DataSourceFactory dataSourceFactory = new DataSourceFactory();
			Properties properties = new Properties();
			properties.load(new ClassPathResource(dbProps).getInputStream());
			logger.info("read datasource configuration finished");
			return dataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			logger.error("Found exception in configuring DB : ", e);
			throw new RuntimeException();
		}
	}

	@Bean(name = "readEntityManager")
	@Primary
	LocalContainerEntityManagerFactoryBean readEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("readDataSource") DataSource dataSource) {
		logger.info("configuring primary read entity manager");
		final LocalContainerEntityManagerFactoryBean entityManager = builder.dataSource(dataSource)
				.packages("com.shubheksha.model").build();
		logger.info("primary read entity manager configuration finished");
		return entityManager;
	}

	@Bean(name = "readTransactionManager")
	@Primary
	PlatformTransactionManager readTransactionManager(
			@Qualifier("readEntityManager") EntityManagerFactory entityManagerFactory) {
		logger.info("configuring primary read transaction manager");
		final JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		logger.info("primary read transaction manager configuring finished");
		return transactionManager;
	}
}
