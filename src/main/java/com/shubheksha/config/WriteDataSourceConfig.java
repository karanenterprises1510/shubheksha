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
		"com.shubheksha.write.repository" }, entityManagerFactoryRef = "writeEntityManager", transactionManagerRef = "writeTransactionManager")
public class WriteDataSourceConfig {
	public static final Logger logger = LoggerFactory.getLogger(WriteDataSourceConfig.class);

	@Value("${spring.shubheksha.write.db.properties}")
	private String dbProps;

	@Bean(name = "writeDataSource")
	@ConfigurationProperties(prefix = "spring.write.datasource")
	DataSource writeDataSource() {
		logger.info("configuring write datasource");
		try {
			DataSourceFactory dataSourceFactory = new DataSourceFactory();
			Properties properties = new Properties();
			properties.load(new ClassPathResource(dbProps).getInputStream());
			logger.info("write datasource configuration finished");
			return dataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			logger.error("Found exception in configuring DB : ", e);
			throw new RuntimeException();
		}
//		return writeDataSource;
	}

	@Bean(name = "writeEntityManager")
	LocalContainerEntityManagerFactoryBean writeEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("writeDataSource") DataSource dataSource) {
		logger.info("configuring primary write entity manager");
		final LocalContainerEntityManagerFactoryBean entityManager = builder.dataSource(dataSource)
				.packages("com.shubheksha.model").build();
		logger.info("primary write entity manager configuration finished");
		return entityManager;
	}

	@Bean(name = "writeTransactionManager")
	PlatformTransactionManager writeTransactionManager(
			@Qualifier("writeEntityManager") EntityManagerFactory entityManagerFactory) {
		logger.info("configuring primary write transaction manager");
		final JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
		logger.info("primary write transaction manager configuring finished");
		return transactionManager;
	}
}
