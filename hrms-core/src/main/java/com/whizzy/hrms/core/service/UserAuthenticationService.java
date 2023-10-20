package com.whizzy.hrms.core.service;

import com.whizzy.hrms.core.domain.dto.UserWithAuthorities;

import java.util.Optional;

public interface UserAuthenticationService {
    Optional<UserWithAuthorities> findByLoginId (String login);
}
