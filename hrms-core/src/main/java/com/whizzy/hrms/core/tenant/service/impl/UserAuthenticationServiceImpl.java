package com.whizzy.hrms.core.tenant.service.impl;

import com.whizzy.hrms.core.tenant.domain.UserWithAuthorities;
import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
import com.whizzy.hrms.core.tenant.service.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

     private final UserAuthenticationRepository userAuthenticationRepository;

    public UserAuthenticationServiceImpl (@Autowired UserAuthenticationRepository userAuthenticationRepository) {
        this.userAuthenticationRepository = userAuthenticationRepository;
    }

    @Override
    @Cacheable(value = LOGGED_IN_USER_CACHE, unless="#result == null")
    public Optional<UserWithAuthorities> findByLoginId(String login) {
        UserWithAuthorities userWithAuthorities = null;
        List<Object[]> users = userAuthenticationRepository.findByLogin(login);
        for (Object[] objects : users) {
            if (userWithAuthorities == null) {
                userWithAuthorities = new UserWithAuthorities(
                        (Long)objects[0],
                        String.valueOf(objects[1]),
                        String.valueOf(objects[2]),
                        String.valueOf(objects[3]),
                        String.valueOf(objects[4]),
                        Boolean.valueOf(objects[5].toString()));
            }
           userWithAuthorities.addAuthority(String.valueOf(objects[6]));
        }

        if(LOG.isDebugEnabled()) {
            String auth;
            if(Objects.nonNull(userWithAuthorities)) {
                auth = userWithAuthorities.getAuthorities().stream().collect(Collectors.joining(COMMA));
            } else {
                auth = EMPTY;
            }
            LOG.debug("Authorities for user {} are {}", login, auth);
        }

       return Optional.ofNullable(userWithAuthorities);
    }
}
