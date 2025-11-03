package org.jsp.ebanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private String[] swaggerPaths = { "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**" };

	@Bean
	SecurityFilterChain security(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.authorizeHttpRequests(x -> x.requestMatchers(swaggerPaths).permitAll().anyRequest().authenticated())
				.formLogin(x -> x.disable()).httpBasic(x -> x.disable()).build();
	}
}
