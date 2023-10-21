package com.whizzy.hrms.core.tenant.service;

import com.whizzy.hrms.core.tenant.domain.UserWithAuthorities;

import java.util.Optional;

public interface UserAuthenticationService {
    Optional<UserWithAuthorities> findByLoginId (String login);
}
