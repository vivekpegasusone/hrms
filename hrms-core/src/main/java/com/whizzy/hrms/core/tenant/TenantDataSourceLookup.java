package com.whizzy.hrms.core.tenant;

import com.whizzy.hrms.core.master.domain.Tenant;
import com.whizzy.hrms.core.master.service.TenantService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.MASTER;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.POOL_NAME_SUFFIX;
import static org.springframework.util.StringUtils.hasLength;

@Component
public class TenantDataSourceLookup extends MapDataSourceLookup {

    @Autowired
    private ApplicationContext applicationContext;

    @EventListener (ContextRefreshedEvent.class)
    public void onContextRefresh(ContextRefreshedEvent contextRefreshedEvent) {
        TenantService tenantService = applicationContext.getBean(TenantService.class);
        List<Tenant> tenants = tenantService.findByActive(true);
        tenants.forEach(t -> {
            HikariDataSource dataSource = createDataSource(t, t.getName());
            addDataSource(t.getName(), dataSource);
        });
    }

    private HikariDataSource createDataSource(Tenant tenant, String schema) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(tenant.getUrl());
        dataSource.setUsername(tenant.getUsername());
        dataSource.setPassword(tenant.getPassword());
        dataSource.setDriverClassName(tenant.getDriverClass());
        dataSource.setMinimumIdle(tenant.getMinIdle());
        dataSource.setMaximumPoolSize(tenant.getMaxPoolSize());
        dataSource.setMaxLifetime(tenant.getMaxLifeTime());
        dataSource.setKeepaliveTime(tenant.getKeepAliveTime());
        dataSource.setConnectionTimeout(tenant.getConnectionTimeout());
        dataSource.setPoolName((hasLength(schema) ? schema : MASTER).concat(POOL_NAME_SUFFIX));
        return dataSource;
    }
}
