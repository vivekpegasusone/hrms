package com.whizzy.hrms.core.filter;

import com.whizzy.hrms.core.tenant.TenantContext;
import com.whizzy.hrms.core.util.JwtUtil;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
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

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTHORIZATION;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTH_URL;

@Component
public class JwtValidatorFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtValidatorFilter.class);

    @Value("${hrms.jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (Objects.nonNull(jwtToken)) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                JwtUtil.UserAndAuthorities user = JwtUtil.validateAndGet(jwtToken, key);

                TenantContext.setTenantId(user.tenantId());
                LOG.info("Tenant identified for tokes as {}.", user.tenantId());

                Authentication auth = new UsernamePasswordAuthenticationToken(user.name(), null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(user.authorities()));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }

        }
        filterChain.doFilter(request, response);
        if(Objects.nonNull(jwtToken)) {
            LOG.info("Tenant identifier {} cleared.", TenantContext.getTenantId());
        }
        TenantContext.clear();
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return AUTH_URL.contains(request.getServletPath());
    }
}
