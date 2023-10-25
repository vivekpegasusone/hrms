package com.whizzy.hrms.core.filter;

import com.whizzy.hrms.core.master.service.TenantService;
import com.whizzy.hrms.core.tenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTH_URL;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.DEFAULT_TENANT;
import static com.whizzy.hrms.core.util.HrmsCoreUtil.findDomainNameFromEmail;

@Component
public class TenantFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(TenantFilter.class);

    private final BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();

    @Autowired
    private TenantService tenantService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authRequest = this.authenticationConverter.convert(request);
        if (authRequest == null) {
            LOG.warn("Failed to find username and password in Basic Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        String domain = findDomainNameFromEmail(authRequest.getName());
        String tenantId = Objects.nonNull(domain) ? tenantService.findTenantIdentifier(domain) : DEFAULT_TENANT;
        LOG.info("Tenant identified as {} at user login", tenantId);
        TenantContext.setTenantId(tenantId);
        filterChain.doFilter(request, response);
        TenantContext.clear();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !AUTH_URL.contains(request.getServletPath());
    }
}
