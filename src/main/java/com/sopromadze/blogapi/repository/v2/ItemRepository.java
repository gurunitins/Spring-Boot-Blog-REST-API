package com.sopromadze.blogapi.repository.v2;

import com.sopromadze.blogapi.model.v2.MItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<MItem, Long> {
//    List<MItem> findByHotelId(Long hotelId);
    List<MItem> findAllByMenuId(Long menuId);

    void deleteAllById(List<Long> itemIds);

    List<MItem> findAllByMenuIdIn(List<Long> menuIds);



}
