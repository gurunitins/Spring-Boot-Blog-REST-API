package com.sopromadze.blogapi.service.v2.impl;

import com.sopromadze.blogapi.exception.BadRequestException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.v2.MItem;
import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.v2.ItemRepository;
import com.sopromadze.blogapi.repository.v2.MenuRepository;
import com.sopromadze.blogapi.service.v2.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sopromadze.blogapi.utils.ObjectTransformUtil.partialMapper;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public MItem get(Long itemId) {

        return itemRepository.findById(itemId).orElseThrow(()-> new ResourceNotFoundException("itemId"));
    }

    @Override
    public MItem create(MItem item,Long menuId) {
        item.setMenuId(menuId);
        item.setId(null);

        return itemRepository.save(item);
    }

    @Override
    public List<MItem> create(List<MItem> item, Long menuId) {
        item.stream().forEach(t-> {
            t.setMenuId(menuId);
            t.setId(null);
        });
        return itemRepository.saveAll(item);
    }

    @Override
    public MItem update(MItem item, Long menuId) {
        item.setMenuId(menuId);

        return itemRepository.save(item);
    }

    @Override
    public List<MItem> getAllActive(Long hotelId) {
        List<MMenu> allActiveMenu = menuRepository.findByHotelId(hotelId);
        List<Long> ids =  allActiveMenu.stream().map(t->t.getId()).collect(Collectors.toList());
        return itemRepository.findAllByMenuIdIn(ids);
    }

    @Override
    public List<MItem> getByMenuId(Long hotelId) {
        return itemRepository.findAllByMenuId(hotelId);
    }


    @Override
    public ApiResponse delete(Long itemId) {
        try{
            itemRepository.deleteById(itemId);
            return new ApiResponse(true,"Items deleted successfully");

        }catch (DataRetrievalFailureException e){
            throw new ResourceNotFoundException("field not available");
        }
        catch (Exception e){
            throw new BadRequestException("Error to function");
        }
    }

    @Override
    public ApiResponse delete(List<Long> itemIds) {
        try{
            itemRepository.deleteAllById(itemIds);
            return new ApiResponse(true,"Items deleted successfully");

        }catch (DataRetrievalFailureException e){
            throw new ResourceNotFoundException("field not available");
        }catch (Exception e){
            throw new BadRequestException("Error to function ");
        }
    }

    @Override
    public MItem partialUpdate(Long menuId, Long itemId, Map<String, Object> updates) {
        MItem item =  itemRepository.findById(itemId).orElseThrow(()-> new ResourceNotFoundException("itemId"));

        partialMapper(updates,item);

        return itemRepository.save(item);
    }
}
