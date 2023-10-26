package com.whizzy.hrms.core.tenant.domain.dto;

public record UserSessionData(Long id, String loginId, String authorities, String tenantId) {}
