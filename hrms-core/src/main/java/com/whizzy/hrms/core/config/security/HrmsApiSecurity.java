package com.whizzy.hrms.core.config.security;

import com.whizzy.hrms.core.exception.security.HrmsAccessDeniedHandler;
import com.whizzy.hrms.core.exception.security.HrmsAuthenticationEntryPoint;
import com.whizzy.hrms.core.filter.JwtFilter;
import com.whizzy.hrms.core.filter.JwtValidatorFilter;
import com.whizzy.hrms.core.filter.TenantFilter;
import com.whizzy.hrms.core.tenant.authprovider.TenantAuthProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AUTHORIZATION;

@Configuration
public class HrmsApiSecurity {

    @Value("${hrms.frontend.url}")
    private String uiUrl;

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private TenantFilter tenantFilter;
    @Autowired
    @Qualifier("tenantAuthProvider")
    private TenantAuthProvider tenantAuthProvider;
    @Autowired
    private JwtValidatorFilter jwtValidatorFilter;
    @Autowired
    private HrmsAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private HrmsAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] ANY_OTHER = {"/**"};
    private static final String[] APP_URL = {"/api/**"};

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName("_csrf");

        httpSecurity
                .securityMatcher(APP_URL)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .maximumSessions(1)
                )
                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .csrf(CsrfConfigurer::disable)
                //.exceptionHandling(exConfig -> exConfig.authenticationEntryPoint(authenticationEntryPoint))
                .exceptionHandling(exConfig -> exConfig.accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtValidatorFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(tenantFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests((request -> request
                        //.requestMatchers(ANY_OTHER).permitAll()
                        .anyRequest().authenticated())
                );
        httpSecurity.authenticationProvider(tenantAuthProvider);
        httpSecurity.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint));
        return httpSecurity.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Collections.singletonList(uiUrl));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setExposedHeaders(Arrays.asList(AUTHORIZATION));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        };
    }
}
