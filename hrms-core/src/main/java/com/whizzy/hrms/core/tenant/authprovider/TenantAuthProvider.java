package com.whizzy.hrms.core.tenant.authprovider;

import com.whizzy.hrms.core.tenant.domain.UserWithAuthorities;
import com.whizzy.hrms.core.exception.InActiveUserException;
import com.whizzy.hrms.core.tenant.service.UserAuthenticationService;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.ACTIVE;

@Component(value = "tenantAuthProvider")
public class TenantAuthProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        return userAuthenticationService
                .findByLoginId(username)
                .map(t -> {
                    if (passwordEncoder.matches(password, t.getPassword())) {
                        return validateUser(t);
                    } else {
                        throw new BadCredentialsException("The password is invalid for user " + username);
                    }
                }).orElseThrow(() -> new BadCredentialsException("No user registered with loginId " + username));


    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication validateUser(UserWithAuthorities userSecurity) {
        if (userSecurity.getStatus().equalsIgnoreCase(ACTIVE)) {
            return new UsernamePasswordAuthenticationToken(
                    userSecurity.getLoginId(), userSecurity.getPassword(),
                    prepareAuthorities(userSecurity.getAuthorities()));

        } else {
            throw new InActiveUserException("User with loginId " + userSecurity.getLoginId() + " is deactivated.");
        }
    }

    private List<GrantedAuthority> prepareAuthorities(Set<String> authoritySet) {
        return authoritySet
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
