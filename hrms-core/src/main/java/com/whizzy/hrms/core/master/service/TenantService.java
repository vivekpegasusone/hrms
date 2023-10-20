package com.whizzy.hrms.core.master.service;

import com.whizzy.hrms.core.master.domain.Tenant;

import java.util.List;

public interface TenantService {
    List<Tenant> findAll();
    List<Tenant> findByActive(boolean isActive);
    String findTenantIdentifier(String domain);
}
