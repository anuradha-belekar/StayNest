package com.app.security;

import java.io.IOException;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component //spring bean - can be injected as dependency in other spring beans
//OncePerRequestFilter-represents a filter which invokes once per every request
@Slf4j
@AllArgsConstructor
public class CustomJWTFilter extends OncePerRequestFilter{
	private final JwtUtils jwtUtils;
	
	protected void doFilterInternal(HttpServletRequest req,HttpServletResponse resp,FilterChain filterchain)throws ServletException,IOException{
		//check httpRequestHeader-Authorization
		String headerValue=req.getHeader("Authorization");
		if(headerValue!=null && headerValue.startsWith("Bearer ")) {
			//jwt exists
			//extracts jwt
			String jwt=headerValue.substring(7);
			log.info("jwt in request header {} ",jwt);
			//validate token->
			//in case of sucess--populate authentication object
			//call jwtutils method
			Authentication authentication=jwtUtils.populateAuthenticationTokenFromJwt(jwt);
			//no exec-
			//invalid token,invalid signature,jwt expire
			log.info("Auth object from jwt {} ",authentication);
			log.info("is auth: {} ",authentication.isAuthenticated());//true
			//store authentication object under spring security
			//ctx holder-
			//scope:
			//current request only since session creation policy stateless
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}
		
		//allow request to continue
		filterchain.doFilter(req, resp);
	}
}
