package com.whizzy.hrms.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtil {

    public static String getCurrentUserLoginId() {
        String loginId = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (Objects.nonNull(securityContext)) {
            Authentication authentication = securityContext.getAuthentication();
            if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
                loginId = authentication.getName();
            }
        }
        return loginId;
    }
}
