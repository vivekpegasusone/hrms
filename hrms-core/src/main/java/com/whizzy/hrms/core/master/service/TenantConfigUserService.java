package com.whizzy.hrms.core.master.service;

import com.whizzy.hrms.core.master.domain.TenantConfigUser;

import java.util.Optional;

public interface TenantConfigUserService {

    Optional<TenantConfigUser> findByLoginId(String loginId);
}
