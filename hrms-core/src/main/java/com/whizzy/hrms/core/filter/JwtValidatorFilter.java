package com.whizzy.hrms.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whizzy.hrms.core.tenant.TenantContext;
import com.whizzy.hrms.core.tenant.domain.dto.UserSessionData;
import com.whizzy.hrms.core.util.JwtUtil;
import com.whizzy.hrms.core.util.LogUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;
import static java.util.Objects.nonNull;

@Component
public class JwtValidatorFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtValidatorFilter.class);

    private final String secretKey;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    public JwtValidatorFilter(@Value("${hrms.jwt.secret}") String secretKey, @Autowired ObjectMapper objectMapper,
                              @Autowired CacheManager cacheManager) {
        this.secretKey = secretKey;//Base64.getEncoder().encodeToString(secretKey.getBytes());;
        this.objectMapper = objectMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserSessionData userData = null;
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (nonNull(jwtToken)) {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            try {
                userData = JwtUtil.validateAndGet(jwtToken, key);
            } catch (SecurityException se) {
                LOG.error("The token is not valid. {}", se.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is not valid.", "Validation Exception", objectMapper);
                LogUtil.logStackTrace(se);
                return;
            } catch (ExpiredJwtException ee) {
                LOG.error("The token is expired. {}", ee.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is expired.", "Validation Exception", objectMapper);
                LogUtil.logStackTrace(ee);
                return;
            } catch (Exception e) {
                LOG.error("Not able to parse token. {}", e.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
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
