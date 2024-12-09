package com.sopromadze.blogapi.service.v2;

import com.sopromadze.blogapi.model.v2.MHotel;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.security.UserPrincipal;

import java.util.Map;

public interface HotelService {

	MHotel create(MHotel user, UserPrincipal currentUser);

//	MHotel update(MHotel newUser, String username, UserPrincipal currentUser);


	MHotel get(Long hotelId);


	MHotel update(Map<String, Object> updates, UserPrincipal currentUser);

	ApiResponse delete(String phone, UserPrincipal currentUser);
}