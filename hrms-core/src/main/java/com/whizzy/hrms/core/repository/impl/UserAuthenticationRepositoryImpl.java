package com.whizzy.hrms.core.repository.impl;

import com.whizzy.hrms.core.repository.UserAuthenticationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

    @Autowired
    private EntityManager entityManager;

    private static String query = """
            select u.first_name, u.last_name, u.login, u.password_hash, u.activated, a.name 
            from an_user u
            join an_user_authority ua on u.id = ua.user_id
            join an_authority a on ua.authority_id = a.id
            where u.login = '%s'
            """;
    @Override
    public List<Object[]> findByLogin(String login) {
        String queryStr = String.format(query, login);
        return entityManager.createNativeQuery(queryStr).getResultList();
    }

}
