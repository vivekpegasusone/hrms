package com.whizzy.hrms.core.master.repository;

import com.whizzy.hrms.core.master.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    List<Tenant> findByActive(boolean isActive);

    Tenant findByDomain(String domain);
}
