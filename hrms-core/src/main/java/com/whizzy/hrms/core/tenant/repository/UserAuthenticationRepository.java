package com.whizzy.hrms.core.tenant.repository;

import java.util.List;

public interface UserAuthenticationRepository {
    List<Object[]> findByLogin(String login);
}
