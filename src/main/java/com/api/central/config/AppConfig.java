package com.api.central.config;
/*
 * Copyright 2016 BSOL Systems- IVMASNG To Present - All rights reserved 
 */
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.api.central.User.UserDetail;
import com.api.central.User.UserInterceptor;
import com.api.central.fileDownload.FileOperationValues;
import com.api.central.security.CORSFilter;


@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.api.central")
public class AppConfig extends WebMvcConfigurerAdapter {

	/*
	 * Oracle DB configuration properties
	 * 
	 * */  
	@Value("${db.driver}")
	private String DB_DRIVER;

	@Value("${db.password}")
	private String DB_PASSWORD;

	@Value("${db.url}")
	private String DB_URL;

	@Value("${db.username}")
	private String DB_USERNAME;
	/*
	 * Hibernate configuration properties
	 * 
	 * */ 
	@Value("${hibernate.dialect}")
	private String HIBERNATE_DIALECT;

	@Value("${hibernate.show_sql}")
	private String HIBERNATE_SHOW_SQL;

	@Value("${hibernate.hbm2ddl.auto}")
	private String HIBERNATE_HBM2DDL_AUTO;

	@Value("${entitymanager.packagesToScan}")
	private String ENTITYMANAGER_PACKAGES_TO_SCAN;

	@Value("${hibernate.session.cxt}")
	private String HIBERNATE_SESSION_CXT;

	@Value("${hibernate.enable_lazy_load_no_trans}")
	private String HIBERNATE_LAZY_LOAD;
	/*
	 * Hibernate C3p0 configuration properties
	 * 
	 * */ 

	@Value("${hibernate.c3p0.max_size}")
	private String C3P0_MAX_SIZE;

	@Value("${hibernate.c3p0.min_size}")
	private String C3P0_MIN_SIZE;

	@Value("${hibernate.c3p0.timeout}")
	private String C3P0_TIMEOUT;
	@Value("${hibernate.c3p0.max_statements}")
	private String C3P0_MAX_STATEMENT;
	@Value("${hibernate.c3p0.idle_test_period}")
	private String C3P0_IDLE_PERIOD;

	@Value("${hibernate.c3p0.acquire_increment}")
	private String C3P0_ACQUIRE_INCREMENT;
	
	
	/*
	 * file Operation properties
	 */
	@Value("${fileDownoad.url}")
	private String downloadBaseLink;
	
	@Value("${fileDownload.location}")
	private String downloadLocation;
	
	@Value("${mail.from}")
	private String mailFrom;
	
	@Value("${mail.subject}")
	private String mailSubject;
	
	@Value("${mail.body}")
	private String mailBody;
	
	
	/*
	 * Datasource configuration properties
	 * 
	 * */ 
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DB_DRIVER);
		dataSource.setUrl(DB_URL);
		dataSource.setUsername(DB_USERNAME);
		dataSource.setPassword(DB_PASSWORD);
		return (DataSource) dataSource;
	}

	/*
	 * SessionFacotory configuration properties
	 * 
	 * */ 
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
		hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		hibernateProperties.put("current_session_context_class", HIBERNATE_SESSION_CXT);
		hibernateProperties.put("hibernate.enable_lazy_load_no_trans", HIBERNATE_LAZY_LOAD);
		/*  hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);*/
		hibernateProperties.put("hibernate.c3p0.max_size", C3P0_MAX_SIZE);
		hibernateProperties.put("hibernate.c3p0.min_size", C3P0_MIN_SIZE);
		hibernateProperties.put("hibernate.c3p0.timeout", C3P0_TIMEOUT);
		hibernateProperties.put("hibernate.c3p0.max_statements", C3P0_MAX_STATEMENT);
		hibernateProperties.put("hibernate.c3p0.idle_test_period", C3P0_IDLE_PERIOD);
		hibernateProperties.put("hibernate.c3p0.acquire_increment", C3P0_ACQUIRE_INCREMENT);

		sessionFactoryBean.setHibernateProperties(hibernateProperties);

		return sessionFactoryBean;
	}

	/*
	 * Hibernate SessionFacotory configuration properties
	 * 
	 * */ 
	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = 
				new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	/*
	 *  Multipart configuration properties
	 * 
	 * */ 

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		System.out.println("Loading the multipart resolver");
		CommonsMultipartResolver multipartResolver  = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1000000);
		return multipartResolver;
	}
	/*
	 *  Filter configuration properties
	 * 
	 * */ 
	@Bean
	public FilterRegistrationBean corsFilterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setName("cors");
		CORSFilter corsFilter = new CORSFilter();
		registrationBean.setFilter(corsFilter);
		registrationBean.setOrder(0);
		return registrationBean;
	}
	
	@Bean
	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
	    return new RedisTokenStore(redisConnectionFactory);
	}
	
	@Bean("user")
	public UserDetail user() {
		return new UserDetail();
	}
	@Bean("userInterceptor")
	public UserInterceptor userInterceptor() {
		return new UserInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor()).addPathPatterns("/api/**");
	}
	
	
	@Bean
	public FileOperationValues getPropertiesValue() {
		return new FileOperationValues(downloadBaseLink, downloadLocation,mailFrom, mailSubject, mailBody);
	}
	
	@Bean
	public RestTemplate getTemplate() {
		return new RestTemplate();
	}
}
