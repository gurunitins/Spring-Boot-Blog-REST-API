package com.sopromadze.blogapi.controller.v2;

import com.sopromadze.blogapi.exception.AppException;
import com.sopromadze.blogapi.exception.BlogapiException;
import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.model.v2.role.MRole;
import com.sopromadze.blogapi.model.v2.role.MRoleName;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.JwtAuthenticationResponse;
import com.sopromadze.blogapi.payload.LoginRequest;
import com.sopromadze.blogapi.payload.SignUpRequest;
import com.sopromadze.blogapi.repository.v2.RRoleRepository;
import com.sopromadze.blogapi.repository.v2.RUserRepository;
import com.sopromadze.blogapi.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v2/auth")
public class CAuthController {
	private static final Logger LOG = LoggerFactory.getLogger(CAuthController.class);
	private static final String USER_ROLE_NOT_SET = "User role not set";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RUserRepository userRepository;

	@Autowired
	private RRoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/login")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getPhoneOrEmail(), loginRequest.getPassword()));
		System.out.println("authentication");

		System.out.println(authentication);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtTokenProvider.generateToken(authentication);

//		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

//		Cookie cookie = new Cookie("Authorization", "Bearer " + jwt);
//		cookie.setHttpOnly(true); // Prevents JavaScript access
//		cookie.setSecure(true); // Ensures the cookie is sent only over HTTPS (use false for local testing)
//		cookie.setPath("/"); // Makes the cookie available to all endpoints
//		cookie.setMaxAge(3600);




		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("/signup")//not in use
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (Boolean.TRUE.equals(userRepository.existsByUserName(signUpRequest.getUsername()))) {
			throw new BlogapiException(HttpStatus.BAD_REQUEST, "Username is already taken");
		}

		if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
			throw new BlogapiException(HttpStatus.BAD_REQUEST, "Email is already taken");
		}

		String firstName = signUpRequest.getFirstName().toLowerCase();

		String lastName = signUpRequest.getLastName().toLowerCase();

		String username = signUpRequest.getUsername().toLowerCase();

		String email = signUpRequest.getEmail().toLowerCase();

		String phone = signUpRequest.getPhone();

		String password = passwordEncoder.encode(signUpRequest.getPassword());

		MUser user = new MUser(firstName, lastName, username, email,phone, password,null);

		List<MRole> roles = new ArrayList<>();

		if (userRepository.count() == 0) {
			roles.add(roleRepository.findByName(MRoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
			roles.add(roleRepository.findByName(MRoleName.ROLE_ADMIN)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		} else {
			roles.add(roleRepository.findByName(MRoleName.ROLE_USER)
					.orElseThrow(() -> new AppException(USER_ROLE_NOT_SET)));
		}

		user.setRoles(roles);

		MUser result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/users/{userId}")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
	}
}
