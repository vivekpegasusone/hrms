package com.whizzy.hrms.core.filter;

import com.whizzy.hrms.core.tenant.TenantContext;
import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;
import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
import com.whizzy.hrms.core.filter.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtParser;
    private final CacheManager cacheManager;
    private final UserAuthenticationRepository userAuthRepo;

    public JwtFilter(@Autowired UserAuthenticationRepository userAuthRepo,
                     @Autowired CacheManager cacheManager,
                     @Autowired JwtUtil jwtParser) {
        this.cacheManager = cacheManager;
        this.userAuthRepo = userAuthRepo;
        this.jwtParser = jwtParser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getName())) {
            Optional<UserAuthorities> optionalUserAuth = userAuthRepo.findByLoginId(authentication.getName());

            String authorityStr = populateAuthorities(authentication.getAuthorities());
            String token = jwtParser.generateToken(optionalUserAuth.get(), TenantContext.getTenantId(), authorityStr);
            response.setHeader(AUTHORIZATION, token);
        }

        filterChain.doFilter(request, response);
        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getName())) {
            cacheManager.getCache(LOGGED_IN_USER_CACHE).evictIfPresent(authentication.getName());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !AUTH_URL.contains(request.getServletPath());
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(COMMA));
    }
}
