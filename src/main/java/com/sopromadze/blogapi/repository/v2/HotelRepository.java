package com.sopromadze.blogapi.repository.v2;

import com.sopromadze.blogapi.model.v2.MHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<MHotel, Long> {

}
