package in.fourbits.etracker;

import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

/**
 * class to initialize and start the springboot application
 * 
 * @author Suraj Acharya
 * 
 * 
 * @SpringBootServletInitializer - An opinionated
 *                               {@link WebApplicationInitializer} to run a
 *                               {@link SpringApplication} from a traditional
 *                               WAR deployment. Binds {@link Servlet},
 *                               {@link Filter} and
 *                               {@link ServletContextInitializer} beans from
 *                               the application context to the servlet
 *                               container.
 * 
 * @Profile - This is for setting spring profile and load environment specific
 *          properties accordingly
 *
 */
@SpringBootApplication
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Profile({ "local", "default" })
public class EtrackerApplication extends SpringBootServletInitializer {

	private static final Logger logger = Logger.getLogger(EtrackerApplication.class);

	@Autowired
	private Environment env;

	// Setting the default value for packageToScan from application.properties
	@Value("${packageToScan}")
	private String packageToScan;

	/**
	 * Main method to launch the SpringBoot Application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(EtrackerApplication.class, args);
	}

	/**
	 * Overridden method from {@link SpringBootServletInitializer}} indicating
	 * the CONFIG class for this application
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EtrackerApplication.class);
	}

	/**
	 * Bean to create a DataSource by taking the JDBC values from application
	 * properties
	 * 
	 * @return DataSource
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		logger.info("Getting jdbc connection values from Properties File (application.properties) ");
		dataSource.setDriverClassName(env.getProperty("jdbc.driverName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	/**
	 * Bean to create HIBERNATE LocalSessionFactory, also mentioning the
	 * Packages to be scanned for entities
	 * 
	 * @return LocalSessionFactoryBean
	 */
	@Bean
	public LocalSessionFactoryBean localSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		logger.info("Getting packagesToScan from Properties File (application.properties) ");
		/*
		 * The package to be scanned for entities
		 */
		sessionFactory.setPackagesToScan(packageToScan);
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		/*
		 * This property logs the HIBERNATE QUERIES
		 */
		hibernateProperties.put("hibernate.show_sql", "true");
		// hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;

	}

	/**
	 * Creating an instance of HibernateTransactionManager by setting
	 * sessionFactory to HIBERNATE LocalSessionFactory
	 * 
	 * @return
	 */
	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(localSessionFactory().getObject());
		return transactionManager;
	}
}
