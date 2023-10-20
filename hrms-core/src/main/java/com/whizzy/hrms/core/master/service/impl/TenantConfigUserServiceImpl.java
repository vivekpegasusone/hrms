package com.whizzy.hrms.core.master.service.impl;

import com.whizzy.hrms.core.master.domain.TenantConfigUser;
import com.whizzy.hrms.core.master.repository.TenantConfigUserRepository;
import com.whizzy.hrms.core.master.service.TenantConfigUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantConfigUserServiceImpl implements TenantConfigUserService {

    @Autowired
    private TenantConfigUserRepository repository;

    @Override
    public Optional<TenantConfigUser> findByLoginId(String loginId) {
        return repository.findByLoginId(loginId);
    }
}
