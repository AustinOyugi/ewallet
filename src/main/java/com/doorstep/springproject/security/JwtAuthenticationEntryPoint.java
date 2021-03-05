package com.doorstep.springproject.security;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse
            , AuthenticationException e) throws IOException {

        LOGGER.error("Responding with unauthorized error. Message - {}", e.getMessage());

        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("status", httpServletResponse.SC_UNAUTHORIZED);
        responseData.put("timestamp", Calendar.getInstance().getTime());
        responseData.put("exception", "Sorry, you are not authorized to access this resource");

        PrintWriter out = httpServletResponse.getWriter();
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");

        out.print(new Gson().toJson(responseData));
        out.flush();

        //httpServletResponse.getOutputStream().println(new ObjectMapper().writeValueAsString(responseData));
    }
}
