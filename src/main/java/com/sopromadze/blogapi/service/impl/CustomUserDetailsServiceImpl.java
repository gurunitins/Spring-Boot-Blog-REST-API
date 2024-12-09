package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.repository.v2.RUserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {
	@Autowired
	private RUserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String emailOrPhone) {
		MUser user = userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with this username or email: %s", emailOrPhone)));


		System.out.println("loadUserByUsername >> "+user);

		return UserPrincipal.create(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserById(Long id) {
		MUser user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with id: %s", id)));

		return UserPrincipal.create(user);
	}
}
