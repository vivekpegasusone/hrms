package com.whizzy.hrms.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.io.IOException;
import java.net.URI;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.APPLICATION_JSON;

public class ResponseUtil {
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

