package com.whizzy.hrms.api.domain.auth;

import com.whizzy.hrms.core.tenant.domain.dto.AuthDetail;

public record AuthDetailToken(AuthDetail authDetail, String securityToken) {}
