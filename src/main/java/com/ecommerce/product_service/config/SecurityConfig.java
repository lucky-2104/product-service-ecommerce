package com.ecommerce.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.product_service.security.JwtAuthFilter;

import org.springframework.http.HttpMethod;
import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
	throws Exception
	
	{
		http
		.csrf(csrf -> csrf.disable())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(
				auth ->
				auth
				.requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				
				)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
}
