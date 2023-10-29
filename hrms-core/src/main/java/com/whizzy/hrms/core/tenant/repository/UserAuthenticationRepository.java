package com.whizzy.hrms.core.tenant.repository;

import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;

import java.util.Optional;

public interface UserAuthenticationRepository {
    Optional<UserAuthorities> findByLoginId(String login);
}
