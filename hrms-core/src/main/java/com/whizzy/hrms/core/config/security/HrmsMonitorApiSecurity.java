package com.whizzy.hrms.core.config.security;

import com.whizzy.hrms.core.provider.auth.MonitoringAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class HrmsMonitorApiSecurity {
    private static final String[] PUBLIC_URL = {"/v3/api-docs/**", "/swagger-ui/**"};
    private static final String[] MONITOR_URL = {"/actuator", "/actuator/health", "/actuator/**"};
    private static final String[] ALLOWED_URL = {"/actuator", "/actuator/**","/v3/api-docs/**", "/swagger-ui/**"};

    @Autowired
    @Qualifier("monitoringAuthProvider")
    private MonitoringAuthProvider monitoringAuthProvider;

    @Bean("monitoringSecurityFilterChain")
    @Order(2)
    public SecurityFilterChain monitoringSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName("_csrf");

        httpSecurity
                .securityMatcher(ALLOWED_URL)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .cors(corsConfig -> corsConfig.disable())
                .csrf(csrf -> csrf.csrfTokenRequestHandler(csrfHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .authorizeHttpRequests((request -> request
                        .requestMatchers(MONITOR_URL).authenticated()
                        .requestMatchers(PUBLIC_URL).permitAll()
                ));

        httpSecurity.authenticationProvider(monitoringAuthProvider);
        httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
