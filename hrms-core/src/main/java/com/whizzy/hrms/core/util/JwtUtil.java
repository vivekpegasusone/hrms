package com.whizzy.hrms.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

public class JwtUtil {

    public record UserAndAuthorities (String tenantId, String name, String authorities) {};

    public static String generateToken(String tenantId, String name, String authorities,
                                       SecretKey secretKey, long tokenExpiryTime)
            throws IllegalArgumentException {
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(JWT_TOKEN)
                .claim(USER_NAME, name)
                .claim(TENANT_ID, tenantId)
                .claim(AUTHORITIES, authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + tokenExpiryTime))
                .signWith(secretKey).compact();
    }

    public static UserAndAuthorities validateAndGet(String jwtToken, SecretKey secretKey) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

        String tenantId = (String)(claims.get(TENANT_ID));
        String username = (String)(claims.get(USER_NAME));
        String authorities = (String) claims.get(AUTHORITIES);
        return new UserAndAuthorities(tenantId, username, authorities);
    }
}

