package com.sopromadze.blogapi.service.v2;

import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface MenuService {

	MMenu create(MMenu user, UserPrincipal currentUser);
	MMenu update(MMenu user, UserPrincipal currentUser);

//	MMenu update(MMenu newUser, String username, UserPrincipal currentUser);

	List<MMenu> getAllInHotel(Long menuId);
	MMenu get(Long menuId);

	List<MMenu> getActive(Long hotelId);

//	MMenu get(Long menuId);

	ApiResponse delete(long menuId, UserPrincipal currentUser);

	MMenu copy(long menuId, UserPrincipal currentUser);

	MMenu partialUpdate(Long menuId, Map<String, Object> updates, UserPrincipal currentUser);
}