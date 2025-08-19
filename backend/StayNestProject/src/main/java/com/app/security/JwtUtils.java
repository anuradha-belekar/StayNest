package com.app.security;



import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.app.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component //bean
@Slf4j
public class JwtUtils {
	//inject props in jwtutils for creating & validating jwt
	//@value=injection of value(<constr-arg name and value:xml tags>)
	//arg-spEL(spring expression language)
	@Value("${jwt.secret.key}")
	private String jwtSecret;
	
	@Value("${jwt.expiration.time}")
	private int jwtExpirationMs;
	
	private SecretKey key;//represents symmetric key
	
	@PostConstruct
	public void init() {
		log.info("key {} exp time {}",jwtSecret,jwtExpirationMs);
		
		//create secrte key instance from keys class
		//key-builder of secret key
		//create a sceret key using HMAC-SHA256 encryption algo
		key=Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}
	
	
	//will be invoked by Usersignin controller,upon successfull 
	//authentication
	public String generateJwtToken(Authentication authentication) {
		log.info("generate jwt token"+authentication);//contains verifed user details
		User userPrincipal=(User) authentication.getPrincipal();
		return Jwts.builder()//jwts:factory class,used to create JWT TOKENS
				.subject((userPrincipal.getUsername())) //setting subject part of token
				.issuedAt(new Date())//sets the jwt claims issued at value of current date
				.expiration(new Date(new Date().getTime()+jwtExpirationMs))//sets jwt claims exp
				//setting a custom claim to add granted authorities
				.claim("authorities", getAuthoritiesInString(userPrincipal.getAuthorities()))
				//setting a custom claim to add a user id(remove it if not required in project
				.signWith(key,Jwts.SIG.HS256) //signs constructed jwt using specified algo with specified key
				//producing Jws(json web signature=signed jwt)
				
				//using token sign in algo: HMAC using SHA-512
				.compact();
				
				
	}

	//invoke by our custom JwtFilter
	public String getUsernameFromJwtToken(Claims claims) {
		return claims.getSubject();
	}
	
	//invoke by our custom JwtFilter
	public Claims validateJwtToken(String jwtToken) {
		Claims claims=Jwts.parser()
				.verifyWith(key)//sets same secret key for jwt signature verification
				.build()
				//returns jwt parser sets with key
				.parseSignedClaims(jwtToken)//return jwt with claims added in body
				.getPayload();//jwt valid returns claims payload
				/*parseClaimsJws - 
				throws:
				  UnsupportedJwtException -if the JWT body | payload does not represent any Claims
				  JWSMalformedJwtException - if the JWT body |payload is not a valid 
				  JWSSignatureException - if the JWT signature validation fails
				  ExpiredJwtException - if the specified JWT is expired
				  IllegalArgumentException - if the JWT claims body | payload is null or empty or only whitespace
				 */
				return claims;
	}

	private List<String> getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}
	
	//this method will be invoked by our custom JWT filter to get list of granted
	// authorities n store it in auth token
	public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims){
		List<String> authorityNamesFromJwt = (List<String>)claims.get("authorities");
		List<GrantedAuthority> authorities = authorityNamesFromJwt.stream()
											.map(SimpleGrantedAuthority::new)
											.collect(Collectors.toList());
		authorities.forEach(System.out::println);
		return authorities;
				
	}
	
	public Authentication populateAuthenticationTokenFromJwt(String jwt) {
		//validate jwt and retrieve jwt body (claims)
		Claims payloadClaims = validateJwtToken(jwt);
		//get username from claims
		String email = getUsernameFromJwtToken(payloadClaims);
		//get granted authorities as a custom claim
		List<GrantedAuthority> authorities = getAuthoritiesFromClaims(payloadClaims);
		//add username/email,null:password granted authorities in authentication object
		UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(email,jwt,authorities);
		System.out.println("is Authenticated"+token.isAuthenticated());//true
		return token;
	}
	
}
