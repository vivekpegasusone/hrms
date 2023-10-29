package com.whizzy.hrms.core.filter.model;

public record UserSessionData(Long id, String loginId, String authorities, String tenantId) {}
