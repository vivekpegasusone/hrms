package com.whizzy.hrms.core.master.service.impl;

import com.whizzy.hrms.core.master.domain.Tenant;
import com.whizzy.hrms.core.master.repository.TenantRepository;
import com.whizzy.hrms.core.master.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.DEFAULT_TENANT;

@Service
public class TenantServiceImpl implements TenantService {
    private static final Logger LOG = LoggerFactory.getLogger(TenantServiceImpl.class);

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public List<Tenant> findAll() {
         return tenantRepository.findAll();
    }

    @Override
    public List<Tenant> findByActive(boolean isActive) {
        return tenantRepository.findByActive(isActive);
    }

    @Override
    public String findTenantIdentifier(String domain) {
        Tenant tenant = tenantRepository.findByDomain(domain);
        return Objects.nonNull(tenant) ? tenant.getName() : DEFAULT_TENANT;
    }
}
