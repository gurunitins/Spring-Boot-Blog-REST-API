package com.sopromadze.blogapi.repository.v2;

import com.sopromadze.blogapi.model.v2.MMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MMenu, Long> {
    List<MMenu> findByHotelId(Long hotelId);

    List<MMenu> findByIdAndIsActiveTrue(Long hotelId);


}
