package com.whizzy.hrms.core.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.DEFAULT_TENANT;

/**
 * This class translates the current tenant into the schema to be used for the data source.
 */
@Component
public class TenantResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        return TenantContext.getTenantId() != null ? TenantContext.getTenantId() : DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
