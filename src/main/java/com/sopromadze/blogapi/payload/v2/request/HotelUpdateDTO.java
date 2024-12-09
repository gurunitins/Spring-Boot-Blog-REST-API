package com.sopromadze.blogapi.payload.v2.request;

import com.sopromadze.blogapi.model.v2.MAddress;
import lombok.Data;
import lombok.NonNull;

@Data
public class HotelUpdateDTO {


    private String fullName;
    private String userName;
    private String email;
    private MAddress address;


    private String phone;
    private String website;

}