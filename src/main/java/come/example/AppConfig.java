package come.example;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "app.springhibernate")  // Enabling component scanning
@EnableAspectJAutoProxy // If you are using Aspect-Oriented Programming (AOP)
public class AppConfig {

    /* DataSource Bean configuration (using C3P0 Connection Pool)
    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/springhibernate?useSSL=false");
        dataSource.setUser("");  // Add your database username here
        dataSource.setPassword("");  // Add your database password here
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(10);
        dataSource.setMaxIdleTime(30000);
        return dataSource;
    }

    // SessionFactory Bean configuration for Hibernate
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("app.springhibernate.model");

        // Hibernate Properties configuration
        java.util.Properties hibernateProperties = new java.util.Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        sessionFactory.setHibernateProperties(hibernateProperties);

        return sessionFactory;
    }

    // Transaction Manager Bean
    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) throws Exception {
        return new DataSourceTransactionManager(dataSource());  // Uses the configured dataSource
    } */
}
