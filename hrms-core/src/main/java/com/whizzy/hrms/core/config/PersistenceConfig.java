package com.whizzy.hrms.core.config;

import com.whizzy.hrms.core.tenant.TenantConnectionProvider;
import com.whizzy.hrms.core.tenant.TenantResolver;
import com.whizzy.hrms.core.master.repository.TenantConfigUserRepository;
import com.whizzy.hrms.core.master.repository.TenantRepository;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.NONE;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.TENANT_PERSISTENT_UNIT;

@Configuration
@EnableJpaRepositories(
        basePackages = { "com.whizzy.hrms" },
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {TenantRepository.class, TenantConfigUserRepository.class})
        }
)
public class PersistenceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private TenantResolver tenantResolver;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TenantConnectionProvider connectionProvider;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();

        emFactory.setPersistenceUnitName(TENANT_PERSISTENT_UNIT);
        emFactory.setPackagesToScan("com.whizzy.hrms");

        emFactory.setJpaVendorAdapter(getJpaVendorAdapter());
        emFactory.setJpaPropertyMap(getJpaPropertyMap());
        emFactory.setJpaProperties(additionalProperties());

        return emFactory;
    }

    @Bean
    @Primary
    public JpaTransactionManager tenantTransactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory emFactory) {
        JpaTransactionManager tenantTransactionManager = new JpaTransactionManager();
        tenantTransactionManager.setEntityManagerFactory(emFactory);
        return tenantTransactionManager;
    }

    private JpaVendorAdapter getJpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    private Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> propertyMap = new HashMap<>(jpaProperties.getProperties());
        propertyMap.put(AvailableSettings.HBM2DDL_AUTO, NONE);
        propertyMap.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, false);
        propertyMap.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        propertyMap.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        return propertyMap;
    }

    private Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        // Any other property specific to vendor and not available via AvailableSettings
        return hibernateProperties;
    }
}