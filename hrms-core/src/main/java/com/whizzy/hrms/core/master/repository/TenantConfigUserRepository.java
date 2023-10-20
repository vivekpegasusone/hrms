package com.whizzy.hrms.core.master.repository;

import com.whizzy.hrms.core.master.domain.Tenant;
import com.whizzy.hrms.core.master.domain.TenantConfigUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantConfigUserRepository extends JpaRepository<TenantConfigUser, Long> {
    Optional<TenantConfigUser> findByLoginId(String loginId);
}
