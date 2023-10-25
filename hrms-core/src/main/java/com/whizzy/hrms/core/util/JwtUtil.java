package com.whizzy.hrms.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;

public class JwtUtil {

    private static final String APP_NAME = "HRMS App";
    private static final String JWT_TOKEN = "JWT Token";
    private static final String APPLICATION_JSON = "application/json";

    public record UserAndAuthorities (String tenantId, String name, String authorities) {}

    public static String generateToken(String tenantId, String name, String authorities,
                                       SecretKey secretKey, long tokenExpiryTime)
            throws IllegalArgumentException {

        long currentTimeInMillis = System.currentTimeMillis();
        Date expiryDate = new Date(currentTimeInMillis + tokenExpiryTime);

        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(JWT_TOKEN)
                .claim(USER_NAME, name)
                .claim(TENANT_ID, tenantId)
                .claim(AUTHORITIES, authorities)
                .setIssuedAt(new Date(currentTimeInMillis))
                .setExpiration(expiryDate)
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

    public static void writeMessageToResponse(HttpServletRequest request, HttpServletResponse response,
                                              HttpStatusCode statusCode, String message, String title,
                                              ObjectMapper objectMapper) throws IOException {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(statusCode, message);
        pd.setTitle(title);
        pd.setInstance(URI.create(request.getRequestURI()));
        response.setContentType(APPLICATION_JSON);
        response.setStatus(statusCode.value());
        response.getWriter().write(objectMapper.writeValueAsString(pd));
    }
}

