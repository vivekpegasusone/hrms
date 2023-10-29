package com.whizzy.hrms.api.controller;

import com.whizzy.hrms.api.domain.auth.AuthDetailToken;
import com.whizzy.hrms.core.tenant.domain.dto.AuthDetail;
import com.whizzy.hrms.core.tenant.service.UserAuthenticationService;
import com.whizzy.hrms.core.util.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTHORIZATION;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final UserAuthenticationService userAuthenticationService;

    public AuthController(@Autowired UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/api/authentication")
    public AuthDetailToken authenticateAndAuthorize(HttpServletResponse response) {
        // spring security ensures that loginId exists and is successful
        String token = response.getHeader(AUTHORIZATION);
        String loginId = SecurityUtil.getCurrentUserLoginId();
        AuthDetail authDetail = userAuthenticationService.findByLoginId(loginId);
        return new AuthDetailToken(authDetail, token);
    }
}
