package com.whizzy.hrms.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whizzy.hrms.core.tenant.TenantContext;
import com.whizzy.hrms.core.filter.model.UserSessionData;
import com.whizzy.hrms.core.filter.util.JwtUtil;
import com.whizzy.hrms.core.util.LogUtil;
import com.whizzy.hrms.core.util.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;
import static java.util.Objects.nonNull;

@Component
public class JwtValidatorFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtValidatorFilter.class);

    private final JwtUtil jwtParser;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    public JwtValidatorFilter(@Autowired ObjectMapper objectMapper,
                              @Autowired CacheManager cacheManager,
                              @Autowired JwtUtil jwtParser) {
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
        this.jwtParser = jwtParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserSessionData userData = null;
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (nonNull(jwtToken)) {
            try {
                userData = jwtParser.validateAndGet(jwtToken);
            } catch (SecurityException se) {
                LOG.error("The token is not valid. {}", se.getLocalizedMessage());
                ResponseUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is not valid.", "Validation Exception", objectMapper);
                LogUtil.logStackTrace(se);
                return;
            } catch (ExpiredJwtException ee) {
                LOG.error("The token is expired. {}", ee.getLocalizedMessage());
                ResponseUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is expired.", "Validation Exception", objectMapper);
                LogUtil.logStackTrace(ee);
                return;
            } catch (Exception e) {
                LOG.error("Not able to parse token. {}", e.getLocalizedMessage());
                ResponseUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        e.getLocalizedMessage(), "Validation Exception", objectMapper);
                LogUtil.logStackTrace(e);
                return;
            }

            if(nonNull(userData) && nonNull(userData.loginId())) {
                LOG.info("Tenant identified for tokes as {}.", userData.tenantId());
                Authentication auth = new UsernamePasswordAuthenticationToken(userData.loginId(), null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(userData.authorities()));

                TenantContext.setTenantId(userData.tenantId());
                SecurityContextHolder.getContext().setAuthentication(auth);
                cacheManager.getCache(LOGGED_IN_USER_CACHE).put(userData.loginId(), userData);
            }
        }

        filterChain.doFilter(request, response);
        TenantContext.clear();
        if(nonNull(userData) && nonNull(userData.loginId())) {
            LOG.info("Session cache evicted for {}.", userData.loginId());
            cacheManager.getCache(LOGGED_IN_USER_CACHE).evictIfPresent(userData.loginId());
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return AUTH_URL.contains(request.getServletPath());
    }
}
