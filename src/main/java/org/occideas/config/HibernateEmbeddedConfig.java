package org.occideas.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Profile("embedded")
@Configuration
@EnableTransactionManagement
public class HibernateEmbeddedConfig {

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource ds){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setPackagesToScan("org.occideas");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans","true");
        hibernateProperties.setProperty("hibernate.connection.CharSet","utf8");
        hibernateProperties.setProperty("hibernate.connection.characterEncoding","utf8");
        hibernateProperties.setProperty("hibernate.id.new_generator_mappings","false");
        hibernateProperties.setProperty("hibernate.ddl-auto","create-drop");
//        hibernateProperties.setProperty("hibernate.format_sql","true");
        hibernateProperties.setProperty("hibernate.jdbc.batch_size","100");
//        hibernateProperties.setProperty("hibernate.generate_statistics","true");
        hibernateProperties.setProperty("hibernate.enhancer.enableDirtyTracking","true");
        hibernateProperties.setProperty("hibernate.enhancer.enableLazyInitialization","true");
        hibernateProperties.setProperty("hibernate.enhancer.enableAssociationManagement","true");
        return hibernateProperties;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

}
