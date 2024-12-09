package com.sopromadze.blogapi.controller.v2;

import com.sopromadze.blogapi.model.v2.MHotel;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.v2.request.HotelUpdateDTO;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.HotelService;
import com.sopromadze.blogapi.service.v2.SUserService;
import com.sopromadze.blogapi.utils.ObjectTransformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/hotel")
public class HotelController {
	private static final Logger LOG = LoggerFactory.getLogger(HotelController.class);

	@Autowired
	private HotelService hotelService;

	private SUserService userService;

	@GetMapping("/mine")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<MHotel> getCurrentHotel(@CurrentUser UserPrincipal currentUser) {
		System.out.println(currentUser.getHotel());
		MHotel hotel = hotelService.get(currentUser.getHotel().getId());

		return new ResponseEntity< >(hotel, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasRole('BOSS')")
	public ResponseEntity<MHotel> createHotel(@Valid @RequestBody MHotel hotel, @CurrentUser UserPrincipal currentUser) {

		MHotel Newhotel = hotelService.create(hotel,currentUser);

		return new ResponseEntity< >(Newhotel, HttpStatus.CREATED);
	}

	@PatchMapping("/{hotelId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
	public ResponseEntity<MHotel> getMenu(@PathVariable(value = "hotelId") Long hotelId, @Valid @RequestBody Map<String, Object> hotel, @CurrentUser UserPrincipal currentUser) {

		MHotel resp = hotelService.update(hotel,currentUser);

		return new ResponseEntity<MHotel>(resp, HttpStatus.OK);
	}

	@DeleteMapping
	@PreAuthorize("hasRole('BOSS')")
	public ResponseEntity<ApiResponse> deleteHotel(@Valid @RequestBody String phone, @CurrentUser UserPrincipal currentUser) {

		ApiResponse resp = hotelService.delete(phone,currentUser);

		return new ResponseEntity<ApiResponse >(resp, HttpStatus.OK);
	}



}
