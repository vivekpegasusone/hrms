package com.whizzy.hrms.core.config;

import com.whizzy.hrms.core.master.domain.Tenant;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.Map;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

@Component
@EnableJpaRepositories(
        basePackages = { "com.whizzy.hrms.core.master.repository"}
)
public class MasterPersistenceConfig {

    @Value("${master.db.config.url}")
    private String url;

    @Value("${master.db.config.driver}")
    private String driverClass;

    @Value("${master.db.config.username}")
    private String username;

    @Value("${master.db.config.password}")
    private String password;

    @Value("${hikari.minimum.idle}")
    private int minimumIdle;

    @Value("${hikari.maximum.pool.size}")
    private int maximumPoolSize;

    @Value("${hikari.maximum.lifetime}")
    private int maxLifetime;

    @Value("${hikari.keep.alive.time}")
    private int keepAliveTime;

    @Value("${hikari.connection.timeout}")
    private int connectionTimeout;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setPersistenceUnitName(MASTER_PERSISTENT_UNIT);
        entityManager.setPackagesToScan(Tenant.class.getPackageName());
        entityManager.setDataSource(dataSource);

        entityManager.setJpaVendorAdapter(getJpaVendorAdapter());
        entityManager.setJpaPropertyMap(getJpaProperties());
        return entityManager;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);
        dataSource.setMinimumIdle(minimumIdle);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMaxLifetime(maxLifetime);
        dataSource.setKeepaliveTime(keepAliveTime);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setPoolName(MASTER.concat(POOL_NAME_SUFFIX));
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private JpaVendorAdapter getJpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    private Map<String, Object> getJpaProperties() {
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put(AvailableSettings.HBM2DDL_AUTO, NONE);
        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, false);

        return properties;
    }
}
