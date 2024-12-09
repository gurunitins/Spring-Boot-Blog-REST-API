package com.sopromadze.blogapi.service.v2;

//import com.sopromadze.blogapi.model.user.MUser;

import com.sopromadze.blogapi.model.v2.MHotel;
import com.sopromadze.blogapi.model.v2.MUser;
import com.sopromadze.blogapi.payload.*;
import com.sopromadze.blogapi.security.UserPrincipal;

import java.util.List;

public interface SUserService {

	UserSummary getCurrentUser(UserPrincipal currentUser);

	List<MUser> getALlUserOfHotel(UserPrincipal currentUser);

	MUser getCurrentUserFull(UserPrincipal currentUser);

	UserIdentityAvailability checkUsernameAvailability(String username);


//	UserProfile getUserProfile(String username);

	UserIdentityAvailability checkPhoneAvailability(String phone);

	MUser addUser(MUser user,UserPrincipal currentUser);

	MUser createUserForHotel(MHotel hotel);

	MUser updateUser(MUser newUser, String username, UserPrincipal currentUser);

	ApiResponse deleteUser(String userId, UserPrincipal currentUser);

	ApiResponse giveAdmin(String username);

	ApiResponse removeAdmin(String username);

	UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}