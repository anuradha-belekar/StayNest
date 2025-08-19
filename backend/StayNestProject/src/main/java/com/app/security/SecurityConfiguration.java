package com.app.security;

import org.springframework.context.annotation.Bean;



import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration //to declare config class use for configuration of class used for security
@EnableWebSecurity //customize spring security for web security support
@EnableMethodSecurity //enable method level annotations (guards url like httprequests)
//(@PreAuthorize , @PostAuthorize..) to specify  authorization rules
//(guards methods in servce,controller,etc)
@AllArgsConstructor
public class SecurityConfiguration {
	
	private final PasswordEncoder passwordEncoder;
	private final CustomJWTFilter customJWTFilter;
	private final JwtAuthEntryPoint jwtAuthEntryPoint;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
		//disable csrf protection (cross site request forgery)
		http.csrf(csrf->csrf.disable());
		//authenticate any request
		http.authorizeHttpRequests(request->
		//permit all swagger, view all properties,user signin,user signup...
		request.requestMatchers("/swagger-ui/**","/v**/api-docs/**"
				,"/users/signin","/users/signup","/otp/generate", "/otp/verify","/users/verify-otp").permitAll()
		//error-public
		.requestMatchers("/error").permitAll()
		
		.requestMatchers("/images/**").permitAll()
		//front end react
		.requestMatchers(HttpMethod.OPTIONS).permitAll()
		//properties-GET-To get all properties-no Authentication
		.requestMatchers(HttpMethod.GET,"/properties").permitAll()
		.requestMatchers(HttpMethod.GET, "/properties/{id}/propertyImages").permitAll() // Public metadata
        .requestMatchers(HttpMethod.GET, "/properties/{id}/propertyImages/{imageId}").permitAll() // Public image data
        //get property by id -customer
		.requestMatchers(HttpMethod.GET,"/properties/{id}").permitAll()
		//update property details -admin
		.requestMatchers(HttpMethod.POST,"/properties").hasRole("OWNER").anyRequest().authenticated()
		);
		//set session creation policy-stateless
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		//add custom jwt filter before usernamepasswordauthfilter
		http.addFilterBefore(customJWTFilter, UsernamePasswordAuthenticationFilter.class);
		//customize errorcode of sc-401 | sc-403 ,in case of authentication failure
		http.exceptionHandling(ex->ex.authenticationEntryPoint(jwtAuthEntryPoint));
		return http.build();
	}

	//configure spring to return spring security authentication manager
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration mgr)throws Exception{
		return mgr.getAuthenticationManager();
	}
	
	
	
}	
