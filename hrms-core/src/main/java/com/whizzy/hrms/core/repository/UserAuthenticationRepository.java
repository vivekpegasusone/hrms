package com.whizzy.hrms.core.repository;

import java.util.List;

public interface UserAuthenticationRepository {
    List<Object[]> findByLogin(String login);
}
