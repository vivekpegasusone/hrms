package com.whizzy.hrms.core.tenant.domain.dto;

import java.util.Set;

public record AuthDetail(String firstName, String lastName, String loginId, Set<String> authorities) {}