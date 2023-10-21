package com.whizzy.hrms.api.controller;

import com.whizzy.hrms.api.domain.auth.AuthDetail;
import com.whizzy.hrms.core.tenant.domain.UserWithAuthorities;
import com.whizzy.hrms.core.tenant.service.UserAuthenticationService;
import com.whizzy.hrms.core.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTHORIZATION;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/api/authentication")
    public AuthDetail authenticateAndAuthorize(HttpServletRequest request, HttpServletResponse response) {
        // spring security ensures that loginId exists and is successful
        String token = response.getHeader(AUTHORIZATION);
        String loginId = SecurityUtil.getCurrentUserLoginId();
        Optional<UserWithAuthorities> optionalUser = userAuthenticationService.findByLoginId(loginId);
        AuthDetail authDetail = modelMapper.map(optionalUser.get(), AuthDetail.class);
        authDetail.setSecurityToken(token);
        return authDetail;
    }
}
