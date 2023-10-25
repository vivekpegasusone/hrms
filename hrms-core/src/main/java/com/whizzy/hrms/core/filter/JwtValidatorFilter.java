package com.whizzy.hrms.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whizzy.hrms.core.tenant.TenantContext;
import com.whizzy.hrms.core.util.JwtUtil;
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

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTHORIZATION;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTH_URL;

@Component
public class JwtValidatorFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtValidatorFilter.class);

    @Value("${hrms.jwt.secret}")
    private String secretKey;

    @Autowired
    private ObjectMapper objectMapper;

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
            } catch (SecurityException se) {
                LOG.error("The token is not valid. {}", se.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is not valid.", "Validation Exception", objectMapper);
                return;
            } catch (ExpiredJwtException ee) {
                LOG.error("The token is expired. {}", ee.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        "The token is expired.", "Validation Exception", objectMapper);
                return;
            } catch (Exception e) {
                LOG.error("The token is not valid. {}", e.getLocalizedMessage());
                JwtUtil.writeMessageToResponse(request, response, HttpStatus.UNAUTHORIZED,
                        e.getLocalizedMessage(), "Validation Exception", objectMapper);
                return;
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
