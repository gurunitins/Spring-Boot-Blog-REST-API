package com.sopromadze.blogapi.payload.v2.request;

import lombok.Data;

@Data
public class MenuUpdateDTO {
    private String name;
    private Boolean isActive;
    private String description;
}