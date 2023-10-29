package com.whizzy.hrms.core.tenant.repository.impl;

import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;
import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.LOGGED_IN_USER_CACHE;

@Repository
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationRepositoryImpl.class);

    @Autowired
    private final EntityManager entityManager;

    public UserAuthenticationRepositoryImpl(@Autowired EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static final String query = """
            select u.id, u.first_name, u.last_name, u.login_id, u.password_hash, u.status, a.name
            from user u join user_access_group ua on u.id = ua.user_id
            join access_group a on ua.access_group_id = a.id where u.login_id = '%s'
            """;
    @Override
    @Cacheable(value = LOGGED_IN_USER_CACHE, unless="#result == null")
    public Optional<UserAuthorities> findByLoginId(String login) {
        UserAuthorities userAuthorities = null;
        String queryStr = String.format(query, login);
        List<Object[]> users = entityManager.createNativeQuery(queryStr).getResultList();
        for (Object[] objects : users) {
            if (userAuthorities == null) {
                userAuthorities = new UserAuthorities(
                        (Long)objects[0],
                        String.valueOf(objects[1]),
                        String.valueOf(objects[2]),
                        String.valueOf(objects[3]),
                        String.valueOf(objects[4]),
                        Boolean.valueOf(objects[5].toString()));
            }
            userAuthorities.addAuthority(String.valueOf(objects[6]));
        }
        return Optional.ofNullable(userAuthorities);
    }
}
