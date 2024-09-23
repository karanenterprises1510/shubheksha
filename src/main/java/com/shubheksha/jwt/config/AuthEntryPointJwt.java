package com.shubheksha.jwt.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthEntryPointJwt.class);
	
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		LOGGER.error("Unauthorized error: {} ", authException.getMessage());
//		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
//        apiError.setMessage(String.format("Login Failed"));
//		throw new Exception("Login Failed");
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		resolver.resolveException(request, response, null, new Exception("Login Failed"));
		//response.error("/exception/loginError");
	}

}
