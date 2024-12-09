package com.sopromadze.blogapi.controller.v2;

import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.InfoRequest;
import com.sopromadze.blogapi.payload.UserIdentityAvailability;
import com.sopromadze.blogapi.payload.UserProfile;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.SUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2/users")
public class CUserController {

	private static final Logger LOG = LoggerFactory.getLogger(CUserController.class);

	@Autowired
	private SUserService userService;

	@GetMapping("/me")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<MUser> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		MUser userSummary = userService.getCurrentUserFull(currentUser);

		return new ResponseEntity< >(userSummary, HttpStatus.OK);
	}


	@GetMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<MUser>> getAllUserByHotel(@CurrentUser UserPrincipal currentUser) {
		System.out.println("currentUser------->"+currentUser);

		List<MUser> userSummary = userService.getALlUserOfHotel(currentUser);
		return new ResponseEntity< >(userSummary, HttpStatus.OK);
	}



	@GetMapping("/checkUsernameAvailability")
	public ResponseEntity<UserIdentityAvailability> checkUsernameAvailability(@RequestParam(value = "username") String username) {
		UserIdentityAvailability userIdentityAvailability = userService.checkUsernameAvailability(username);

		return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
	}

	@GetMapping("/checkPhoneAvailability")
	public ResponseEntity<UserIdentityAvailability> checkEmailAvailability(@RequestParam(value = "phone") String phone) {
		UserIdentityAvailability userIdentityAvailability = userService.checkPhoneAvailability(phone);
		return new ResponseEntity< >(userIdentityAvailability, HttpStatus.OK);
	}


	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MUser> addUser(@Valid @RequestBody MUser user,@CurrentUser UserPrincipal currentUser) {
		MUser newUser = userService.addUser(user,currentUser);

		return new ResponseEntity< >(newUser, HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<MUser> updateUser(
			@Valid @RequestBody MUser newUser,
			@PathVariable(value = "userId") String username,
			@CurrentUser UserPrincipal currentUser) {
		MUser updatedUSer = userService.updateUser(newUser, username, currentUser);

		return new ResponseEntity< >(updatedUSer, HttpStatus.OK);
	}

	@DeleteMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "userId") String userId,
			@CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = userService.deleteUser(userId, currentUser);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/giveAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> giveAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.giveAdmin(username);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/{username}/takeAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> takeAdmin(@PathVariable(name = "username") String username) {
		ApiResponse apiResponse = userService.removeAdmin(username);

		return new ResponseEntity< >(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/setOrUpdateInfo")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<UserProfile> setAddress(@CurrentUser UserPrincipal currentUser,
			@Valid @RequestBody InfoRequest infoRequest) {
		UserProfile userProfile = userService.setOrUpdateInfo(currentUser, infoRequest);

		return new ResponseEntity< >(userProfile, HttpStatus.OK);
	}

}
