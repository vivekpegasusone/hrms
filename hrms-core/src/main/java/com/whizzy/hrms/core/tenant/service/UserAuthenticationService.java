package com.whizzy.hrms.core.tenant.service;

import com.whizzy.hrms.core.tenant.domain.dto.AuthDetail;

public interface UserAuthenticationService {
    AuthDetail findByLoginId (String login);
}
