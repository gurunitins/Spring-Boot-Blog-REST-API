package com.sopromadze.blogapi.service.v2.impl;

import com.sopromadze.blogapi.exception.AccessDeniedException;
import com.sopromadze.blogapi.model.v2.MHotel;
import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.v2.HotelRepository;
import com.sopromadze.blogapi.repository.v2.RUserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.HotelService;
import com.sopromadze.blogapi.service.v2.SUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static com.sopromadze.blogapi.utils.ObjectTransformUtil.partialMapper;

@Service
public class HotelServiceImpl implements HotelService {
    private static final Logger LOG = LoggerFactory.getLogger(HotelServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RUserRepository userRepository;

    @Autowired
    private SUserService userService;

    @Override
    public MHotel get(Long hotelId) {

        return hotelRepository.getOne(hotelId);
    }

    @Override
    public MHotel create(MHotel hotel, UserPrincipal currentUser) {


        if((hotel.getId() == null || hotel.getId() < 1) && !userService.checkPhoneAvailability(hotel.getPhone()).getAvailable()){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "Phone nnumber already taken " + hotel.getPhone());
            throw new AccessDeniedException(apiResponse);

        }
        MHotel updatedHotel= hotelRepository.save(hotel);

        userService.createUserForHotel(updatedHotel);

        return updatedHotel;
    }

    @Override
    public MHotel update(Map<String, Object> updates, UserPrincipal currentUser) {
        MHotel hotel= hotelRepository.getOne(currentUser.getHotel().getId());

        partialMapper(updates,hotel);

        MHotel updatedHotel= hotelRepository.save(hotel);

        return updatedHotel;
    }

    @Override
    public ApiResponse delete(String phone, UserPrincipal currentUser) {

        return new ApiResponse(Boolean.TRUE, "You successfully deleted Hotel of: " + phone);

    }

}
