package com.whizzy.hrms.core.master.authprovider;

import com.whizzy.hrms.core.master.domain.TenantConfigAuthority;
import com.whizzy.hrms.core.master.service.TenantConfigUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component("monitoringAuthProvider")
public class MonitoringAuthProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TenantConfigUserService tenantConfigUserService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        return tenantConfigUserService
                .findByLoginId(username)
                .map(t -> {
                    if (passwordEncoder.matches(password, t.getPassword())) {
                        return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(t.getAuthorityList()));
                    } else {
                        throw new BadCredentialsException("The password is invalid for user " + username);
                    }
                }).orElseThrow(() -> new BadCredentialsException("No user registered with loginId " + username));
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<TenantConfigAuthority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (TenantConfigAuthority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
