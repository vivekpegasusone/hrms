package com.whizzy.hrms.core.tenant;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is responsible for providing tenant-specific connection handling in a multi-tenant
 * application. The tenant distinction is realized by using tenant id.
 */
@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

    private static final long serialVersionUID = -5010963750867167334L;

    //private final EncryptionService encryptionService;

//    @Value("${encryption.secret}")
//    private String secret;
//
//    @Value("${encryption.salt}")
//    private String salt;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TenantDataSourceLookup dataSourceLookup;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        return dataSourceLookup.getDataSource(tenantIdentifier).getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema(tenantIdentifier);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(aClass);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        if ( MultiTenantConnectionProvider.class.isAssignableFrom(aClass) ) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException( aClass );
        }
    }
}
