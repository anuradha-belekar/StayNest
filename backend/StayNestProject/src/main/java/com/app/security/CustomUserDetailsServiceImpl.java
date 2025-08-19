package com.app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.custom_exceptions.ApiException;
import com.app.dao.UserDao;
import com.app.entities.User;

import lombok.AllArgsConstructor;


@Service //springbean containing buiseness logic
@Transactional
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService{
	private final UserDao userDao;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//invoke dao's method
		User user=userDao.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Invalid Email"));
		return user;
	}

}
