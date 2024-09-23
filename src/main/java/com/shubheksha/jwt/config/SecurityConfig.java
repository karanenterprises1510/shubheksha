package com.shubheksha.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.shubheksha.config.ShubhekshaPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	private AuthenticationConfiguration configuration;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new ShubhekshaPasswordEncoder();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated()).httpBasic();
//		return http.build();
//	}

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		return http
//				.csrf()
//				.disable().cors().and()
//				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
//				.exceptionHandling()
//				.authenticationEntryPoint(unauthorizedHandler)
//				.and()
//		.authorizeHttpRequests((authorize) -> authorize
//			.requestMatchers("/process/fetch-all-campaign")
//			.authenticated()
//			.and()
//			.sessionManagement()
//			.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//			.maximumSessions(1)
//			.maxSessionsPreventsLogin(true)).build();
//	}

	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		return http
				.authorizeRequests(request -> request.requestMatchers("/process/fetch-all-campaign").permitAll()
						.anyRequest().authenticated())
				.csrf().disable().cors().and()
				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                        .invalidSessionUrl("/invalidSession.htm")
						.maximumSessions(1).maxSessionsPreventsLogin(true))
				.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/category/get-parent-categories")
				.requestMatchers("/category/get-child-categories").requestMatchers("/category/get-all-categories")
				.requestMatchers("/product/get-products").requestMatchers("/product/product-detail")
				.requestMatchers("/cart/save").requestMatchers("/cart/cart-detail").requestMatchers("/order/save")
				.requestMatchers("/order/order-detail").requestMatchers("/order/approve")
				.requestMatchers("/order/reject").requestMatchers("/product/update-product-inventory")
				.requestMatchers("/product/get-product-inventory")
				.requestMatchers("/**");
	}
}
