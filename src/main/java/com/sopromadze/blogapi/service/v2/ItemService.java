package com.sopromadze.blogapi.service.v2;

import com.sopromadze.blogapi.model.v2.MItem;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.security.UserPrincipal;

import java.util.List;
import java.util.Map;

public interface ItemService {

	MItem get(Long itemId);

	MItem create(MItem user, Long MenuId);

	List<MItem> create(List<MItem> item, Long menuId);


	MItem update(MItem item, Long menuId);

	List<MItem> getAllActive(Long hotelId);

	List<MItem> getByMenuId(Long hotelId);

	ApiResponse delete(Long itemId);

	ApiResponse delete(List<Long> itemIds);

	MItem partialUpdate(Long menuId, Long itemId, Map<String, Object> updates);
}