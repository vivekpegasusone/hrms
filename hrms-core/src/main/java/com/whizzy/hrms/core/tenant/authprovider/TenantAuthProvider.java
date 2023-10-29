package com.whizzy.hrms.core.tenant.authprovider;

import com.whizzy.hrms.core.exception.InActiveUserException;
import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;
import com.whizzy.hrms.core.tenant.repository.UserAuthenticationRepository;
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

import static com.whizzy.hrms.core.util.HrmsCoreConstants.IN_ACTIVE;

@Component(value = "tenantAuthProvider")
public class TenantAuthProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationRepository userAuthRepository;

    public TenantAuthProvider (@Autowired PasswordEncoder passwordEncoder,
                               @Autowired UserAuthenticationRepository userAuthRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        return userAuthRepository
                .findByLoginId(username)
                .map(t -> {
                    if (passwordEncoder.matches(password, t.getPassword())) {
                        if(isValidUser(t)) {
                            return new UsernamePasswordAuthenticationToken(
                                    t.getLoginId(), t.getPassword(),
                                    prepareAuthorities(t.getAuthorities()));
                        } else {
                            throw new InActiveUserException("User with loginId " + t.getLoginId() + " is deactivated.");
                        }
                    } else {
                        throw new BadCredentialsException("The password is invalid for user " + username);
                    }
                }).orElseThrow(() -> new BadCredentialsException("No user registered with loginId " + username));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private boolean isValidUser(UserAuthorities userAuth) {
        return !userAuth.getStatus().equalsIgnoreCase(IN_ACTIVE);
    }

    private List<GrantedAuthority> prepareAuthorities(Set<String> authoritySet) {
        return authoritySet
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
