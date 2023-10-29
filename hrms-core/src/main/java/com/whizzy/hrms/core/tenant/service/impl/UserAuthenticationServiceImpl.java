package com.whizzy.hrms.core.tenant.service.impl;

import com.whizzy.hrms.core.exception.EntityNotFoundException;
import com.whizzy.hrms.core.tenant.domain.dto.AuthDetail;
import com.whizzy.hrms.core.tenant.domain.mapper.UserAuthoritiesToAuthDetail;
import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
import com.whizzy.hrms.core.tenant.service.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);

     private final UserAuthenticationRepository userAuthRepo;

    public UserAuthenticationServiceImpl (@Autowired UserAuthenticationRepository userAuthRepo) {
        this.userAuthRepo = userAuthRepo;
    }

    @Override
    public AuthDetail findByLoginId(String login) {
        return userAuthRepo.findByLoginId(login)
                .map(UserAuthoritiesToAuthDetail::mapToAuthDetail)
                .orElseThrow(() -> new EntityNotFoundException("No user registered with loginId " + login));

    }
}
