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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${hrms.jwt.token.expiry.in.milli}")
    private long tokenExpiryTime;

    @Value("${hrms.jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            String authorityStr = populateAuthorities(authentication.getAuthorities());
            response.setHeader(AUTHORIZATION, JwtUtil.generateToken(TenantContext.getTenantId(), authentication.getName(), authorityStr, key, tokenExpiryTime));
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !AUTH_URL.contains(request.getServletPath());
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(COMMA));
    }
}
