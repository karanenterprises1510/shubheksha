package com.shubheksha.jwt.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthTokenFilter extends OncePerRequestFilter{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
			try {
				String jwt = parseJwt(request);
				if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
					Date issuedDate = jwtUtils.getIssuedDate(jwt);
					Date expiryDate = jwtUtils.getExpiryDate(jwt);

					LocalDateTime issuedDateTime = converterDateToLocalDateTime(issuedDate);
					LocalDateTime expiryDateTime = converterDateToLocalDateTime(expiryDate);
					LOGGER.info(issuedDateTime + " " + expiryDateTime);
					String userName = jwtUtils.getUserNameFromJwtToken(jwt);
//					String audience = jwtUtils.getAudience(jwt);
					
					UserDetails userDetails = null;
	                
	                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                      userDetails, null, userDetails.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	                SecurityContextHolder.getContext().setAuthentication(authentication);
				} 
			}
			catch (IllegalArgumentException e){
				LOGGER.error("Unable to get token from request");
				return;
			}
			catch (ExpiredJwtException ex){
				LOGGER.error("token has been expired.");
				return;
			}
			catch (MalformedJwtException e){
				LOGGER.error("Unable to get token from request");
				return;
			}

	          filterChain.doFilter(request, response);
	}
	
	private LocalDateTime converterDateToLocalDateTime(Date date) {
		return date.toInstant()
			      	.atZone(ZoneId.systemDefault())
			      	.toLocalDateTime();
	}


	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

}
