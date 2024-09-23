package com.shubheksha.jwt.config;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${engine.app.jwtSecret}")
	private String jwtSecret;

	@Value("${engine.app.jwtExpirationMs}")
	private long jwtExpirationMs;
	
//	public String generateJwtToken(Authentication authentication) {
////			UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
////				return Jwts.builder()
////					//.setSubject((userPrincipal.getUsername()))
////					.setSubject((userPrincipal.getUsername()))
////					.setIssuedAt(new Date())
////					.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
////					.signWith(SignatureAlgorithm.HS512, jwtSecret)
////					.compact();
//	}
	
//	public String getUserNameFromJwtToken(String token) {
//		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public Date getIssuedDate(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getIssuedAt();
	}
	
	public Date getExpiryDate(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
	}
	
	public String getAudience(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getAudience();
	}

	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			LOGGER.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOGGER.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOGGER.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOGGER.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public Boolean isTokenExpired(String token){
		final Date expiryDate =  getExpiryDate(token);
		return expiryDate.before(new Date());
	}
	
}
