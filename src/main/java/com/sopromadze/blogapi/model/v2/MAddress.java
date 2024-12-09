package com.sopromadze.blogapi.model.v2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "t_address")
public class MAddress  {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "door_no")
    private String doorNo;

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String pincode;

    private String lat;

    private String lng;

    @JsonIgnore
    public Long getId() {
        return id;
    }

}
