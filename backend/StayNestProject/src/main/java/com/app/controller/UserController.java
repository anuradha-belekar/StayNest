package com.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AuthResp;
import com.app.dto.OtpRequestDto;
import com.app.dto.SignInDto;
import com.app.dto.SignUpDto;
import com.app.dto.UserRespDto;
import com.app.security.JwtUtils;
import com.app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final BookingController bookingController;
	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

    
	
	@PostMapping("/signin")
	@Operation(description = "User sign in")
	public ResponseEntity<?> userSignIn(@RequestBody SignInDto dto){
		System.out.println("in sign in"+dto);
		//invoke AuthenticationManger's authenticate()
		/*
		 * API
		 * 1.1 Authentication authenticate(Authentication auth) 
		 * throws AuthenticationException
		 * i/p - not yet verified credentails
		 * o/p - verified credentials
		 * 1.2 Authentication : i/f
		 * Implemented by UserNamePasswordAuthenticationToken
		 * (String userName|email,String password) 
		 * 
		 */
		Authentication authToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
		System.out.println("Before - "+authToken.isAuthenticated());//false
		Authentication validAuth = authenticationManager.authenticate(authToken);
		//success=>
		System.out.println("After - "+authToken.isAuthenticated());//true
		System.out.println(validAuth);//userdetails:userentity
		//create signed jwt and send it in a response
		return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResp("succesfull login", jwtUtils.generateJwtToken(validAuth)));

	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody @Valid SignUpDto dto){
		System.out.println("in sign up"+dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(dto));
	}
	
	@PostMapping("/verify-otp")
    @Operation(description = "Verify OTP for user signup")
    public ResponseEntity<?> verifyUserOtp(@RequestBody @Valid OtpRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.verifyUserOtp(dto.getEmail(), dto.getOtpValue()));
    }
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "get all users")
	public ResponseEntity<List<UserRespDto>> getAllUsers(){
		System.out.println("in get all users");
		return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
	}
	
	
	@DeleteMapping("{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(description = "delete users")
	public ResponseEntity<?> deleteUser(@PathVariable Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
	}

}
