package com.whizzy.hrms.core.tenant.repository.impl;

import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

    @Autowired
    private EntityManager entityManager;

    private static final String query = """
            select u.first_name, u.last_name, u.login_id, u.password_hash, u.status, a.name
            from user u
            join user_access_group ua on u.id = ua.user_id
            join access_group a on ua.access_group_id = a.id
            where u.login_id = '%s'
            """;
    @Override
    public List<Object[]> findByLogin(String login) {
        String queryStr = String.format(query, login);
        return entityManager.createNativeQuery(queryStr).getResultList();
    }

}
