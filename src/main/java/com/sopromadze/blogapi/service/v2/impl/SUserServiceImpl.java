package com.sopromadze.blogapi.service.v2.impl;

import com.sopromadze.blogapi.exception.*;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.v2.MHotel;
import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.model.v2.role.MRole;
import com.sopromadze.blogapi.model.v2.role.MRoleName;
import com.sopromadze.blogapi.payload.*;
import com.sopromadze.blogapi.repository.v2.RRoleRepository;
import com.sopromadze.blogapi.repository.v2.RUserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.SUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SUserServiceImpl implements SUserService {
	private static final Logger LOG = LoggerFactory.getLogger(SUserServiceImpl.class);

	@Autowired
	private RUserRepository userRepository;

	@Autowired
	private RRoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserSummary getCurrentUser(UserPrincipal currentUser) {
		return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
				currentUser.getLastName());
	}
	@Override
	public List<MUser> getALlUserOfHotel(UserPrincipal currentUser) {
		List<MUser> users= userRepository.findByHotel_Id(currentUser.getHotel().getId());
		if (users.isEmpty()) {
			throw new ResourceNotFoundException("No users found for hotel ID " + currentUser.getHotel().getId());
		}
		return users;
	}

	@Override
	public MUser getCurrentUserFull(UserPrincipal currentUser) {
		return userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new ResourceNotFoundException("resour not found with "+currentUser.getId()));
//		return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFirstName(),
//				currentUser.getLastName());
	}

	@Override
	public UserIdentityAvailability checkUsernameAvailability(String username) {
		Boolean isAvailable = !userRepository.existsByUserName(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@Override
	public UserIdentityAvailability checkPhoneAvailability(String phone) {
		Boolean isAvailable = !userRepository.existsByPhone(phone);
		return new UserIdentityAvailability(isAvailable);
	}


	@Override
	public MUser addUser(MUser user,UserPrincipal currentUser) {
		if (userRepository.existsByUserName(user.getUserName())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Username is already taken");
			throw new BadRequestException(apiResponse);
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Email is already taken");
			throw new BadRequestException(apiResponse);
		}

		List<MRole> roles = new ArrayList<>();
		roles.add(
				roleRepository.findByName(MRoleName.ROLE_USER).orElseThrow(() -> new AppException("MUser role not set")));
		user.setRoles(roles);
		user.setHotel(currentUser.getHotel());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public MUser createUserForHotel(MHotel hotel) {


		if (userRepository.existsByPhone(hotel.getPhone())) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Phone No is already taken");
			throw new BadRequestException(apiResponse);
		}

		String password = passwordEncoder.encode("1234567890");

		MUser user = new MUser(hotel.getUserName(), "", hotel.getFullName(), hotel.getEmail(), hotel.getPhone(), password,hotel);

		List<MRole> roles = new ArrayList<>();

		roles.add(roleRepository.findByName(MRoleName.ROLE_USER)
					.orElseThrow(() -> new AppException("Unable to set User with Role")));
		roles.add(roleRepository.findByName(MRoleName.ROLE_ADMIN)
					.orElseThrow(() -> new AppException("Unable to set User with Role")));

		user.setRoles(roles);

		MUser result = userRepository.save(user);

		return result;

	}

	@Override
	public MUser updateUser(MUser newUser, String username, UserPrincipal currentUser) {
		MUser user = userRepository.getUserByName(username);
		if (user.getId().equals(currentUser.getId())
				|| currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			user.setFirstName(newUser.getFirstName());
			user.setLastName(newUser.getLastName());
			user.setPassword(passwordEncoder.encode(newUser.getPassword()));
			user.setAddress(newUser.getAddress());
			user.setPhone(newUser.getPhone());

			return userRepository.save(user);

		}

		ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + username);
		throw new UnauthorizedException(apiResponse);

	}

	@Override
	public ApiResponse deleteUser(String emailId, UserPrincipal currentUser) {
		MUser user = userRepository.findByEmail(emailId)
				.orElseThrow(() -> new ResourceNotFoundException("MUser", "id", emailId));
		if (!user.getId().equals(currentUser.getId()) || !currentUser.getAuthorities()
				.contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
			ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete profile of: " + emailId);
			throw new AccessDeniedException(apiResponse);
		}

		userRepository.deleteById(user.getId());

		return new ApiResponse(Boolean.TRUE, "You successfully deleted profile of: " + emailId);
	}

	@Override
	public ApiResponse giveAdmin(String username) {
		return null;
	}

	@Override
	public ApiResponse removeAdmin(String username) {
		return null;
	}

	@Override
	public UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest) {
		return null;
	}
//
}
