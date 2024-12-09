package com.sopromadze.blogapi.controller.v2;

import com.sopromadze.blogapi.model.v2.MItem;
import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.v2.request.MenuUpdateDTO;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.ItemService;
import com.sopromadze.blogapi.service.v2.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/item")
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private MenuService menuService;

	@GetMapping
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<List<MItem>> getActiveMenuItem(@CurrentUser UserPrincipal currentUser) {
		List<MMenu> menu = menuService.getActive(currentUser.getHotel().getId());
		List ActiveItems = menu.stream().map(t-> t.getItems()).collect(Collectors.toList());

		return new ResponseEntity< >(ActiveItems, HttpStatus.OK);
	}


	@GetMapping("/{itemId}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<MItem> getSingleItem(
			@PathVariable(value = "itemId") Long itemId) {

		MItem newitem = itemService.get( itemId);

		return new ResponseEntity<>(newitem, HttpStatus.CREATED);
	}

	@DeleteMapping("/{itemId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<ApiResponse> deleteMenu(@PathVariable(value = "itemId") Long itemId) {

		ApiResponse resp = itemService.delete(itemId);

		return new ResponseEntity<ApiResponse >(resp, HttpStatus.OK);
	}

	@DeleteMapping("/multiple")
	@PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<ApiResponse> deleteMenu( @Valid @RequestBody List<Long> ids) {

		ApiResponse resp = itemService.delete(ids);

		return new ResponseEntity<ApiResponse >(resp, HttpStatus.OK);
	}
}
