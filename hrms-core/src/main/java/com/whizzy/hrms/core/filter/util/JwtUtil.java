package com.whizzy.hrms.core.filter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;
import com.whizzy.hrms.core.filter.model.UserSessionData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;
@Component
public class JwtUtil {

    private static final String APP_NAME = "HRMS App";
    private static final String JWT_TOKEN = "JWT Token";

    private final long tokenExpiryTime;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;
    private final ObjectMapper objectMapper;

    //private final String secretKey;
    //this.secretKey = secretKey;//Base64.getEncoder().encodeToString(secretKey.getBytes());

    public JwtUtil(@Value("${hrms.jwt.secret}") String secretKey,
                   @Autowired ObjectMapper objectMapper,
                   @Value("${hrms.jwt.token.expiry.in.milli}") long tokenExpiryTime) {
        this.objectMapper = objectMapper;
        this.tokenExpiryTime = tokenExpiryTime;
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().verifyWith(this.secretKey).build();
    }
    public String generateToken(UserAuthorities user, String tenantId, String authorities)
            throws IllegalArgumentException {

        long currentTimeInMillis = System.currentTimeMillis();
        Date expiryDate = new Date(currentTimeInMillis + tokenExpiryTime);

        return Jwts.builder()
                .issuer(APP_NAME)
                .subject(JWT_TOKEN)
                .claim(ID, user.getId())
                .claim(LOGIN_ID, user.getLoginId())
                .claim(TENANT_ID, tenantId)
                .claim(AUTHORITIES, authorities)
                .issuedAt(new Date(currentTimeInMillis))
                .expiration(expiryDate)
                .signWith(secretKey).compact();
    }

    public UserSessionData validateAndGet(String jwtToken) {
        Claims claims = jwtParser
                .parseSignedClaims(jwtToken)
                .getPayload();

        Long id = ((Integer)claims.get(ID)).longValue();
        String tenantId = (String)(claims.get(TENANT_ID));
        String username = (String)(claims.get(LOGIN_ID));
        String authorities = (String) claims.get(AUTHORITIES);
        return new UserSessionData(id, username, authorities, tenantId);
    }
}

