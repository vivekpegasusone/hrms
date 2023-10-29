package com.whizzy.hrms.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;
import com.whizzy.hrms.core.filter.model.UserSessionData;
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

    public static String generateToken(UserAuthorities user, String tenantId, String authorities,
                                       SecretKey secretKey, long tokenExpiryTime)
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

    public static UserSessionData validateAndGet(String jwtToken, SecretKey secretKey) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        Long id = ((Integer)claims.get(ID)).longValue();
        String tenantId = (String)(claims.get(TENANT_ID));
        String username = (String)(claims.get(LOGIN_ID));
        String authorities = (String) claims.get(AUTHORITIES);
        return new UserSessionData(id, username, authorities, tenantId);
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

