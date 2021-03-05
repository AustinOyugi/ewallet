package com.doorstep.springproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest
            , HttpServletResponse httpServletResponse
            , AuthenticationException authenticationException) throws IOException, ServletException {

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("status", httpServletResponse.SC_UNAUTHORIZED);
        responseData.put("timestamp", Calendar.getInstance().getTime());
        responseData.put("exception", authenticationException.getMessage());

        httpServletResponse.getOutputStream().println(new ObjectMapper().writeValueAsString(responseData));
    }
}
