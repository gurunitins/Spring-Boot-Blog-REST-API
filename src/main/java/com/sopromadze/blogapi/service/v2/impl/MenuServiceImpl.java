package com.sopromadze.blogapi.service.v2.impl;

import com.sopromadze.blogapi.exception.AccessDeniedException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.v2.MItem;
import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.repository.v2.HotelRepository;
import com.sopromadze.blogapi.repository.v2.ItemRepository;
import com.sopromadze.blogapi.repository.v2.MenuRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.sopromadze.blogapi.utils.ObjectTransformUtil.partialMapper;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger LOG = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public MMenu create(MMenu menu, UserPrincipal currentUser) {
        menu.setId(null);
        menu.setHotelId(currentUser.getHotel().getId());

        if ( !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete Menu");
            throw new AccessDeniedException(apiResponse);
        }
        return menuRepository.save(menu);
    }

    @Override
    public MMenu update(MMenu menu, UserPrincipal currentUser) {
        menu.setHotelId(currentUser.getHotel().getId());
        this.checkPermission(menu.getId(), currentUser);
        return menuRepository.save(menu);
    }

    @Override
    public List<MMenu> getAllInHotel(Long hotelId) {
        List<MMenu> menus = menuRepository.findByHotelId(hotelId);
        return menus != null ? menus : Collections.emptyList();
    }

    @Override
    public MMenu get(Long menuId) {
        return menuRepository.getOne(menuId);
    }

    @Override
    public List<MMenu> getActive(Long hotelId) {

        List<MMenu> menus = menuRepository.findByIdAndIsActiveTrue(hotelId);//orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: "+hotelId));
        if (menus.isEmpty()) {
            throw new ResourceNotFoundException("No active hotel found with id: " + hotelId);
        }
        return menus;
    }

    @Override
    public ApiResponse delete(long menuId, UserPrincipal currentUser) {
        if ( !currentUser.getAuthorities()
                .contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete menu  " );
            throw new AccessDeniedException(apiResponse);
        }
        this.checkPermission(menuId, currentUser);
        menuRepository.deleteById(menuId);
        return new ApiResponse(Boolean.TRUE, "You successfully deleted Hotel " );
    }

    @Override
    public MMenu copy(long menuId, UserPrincipal currentUser) {

        this.checkPermission(menuId, currentUser);

        MMenu currMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("menuId not found with "+menuId));
        currMenu.setId(null);

        List<MItem> items = itemRepository.findAllByMenuId(currMenu.getId());
        items.forEach(t->t.setId(null));

        currMenu.setItems(items);
        return menuRepository.save(currMenu);

    }

    @Override
    public MMenu partialUpdate(Long menuId, Map<String, Object> updates, UserPrincipal currentUser) {
        this.checkPermission(menuId, currentUser);

        MMenu existingMenu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));
        System.out.println("\n\nexistingMenu"+existingMenu+"\n");
        partialMapper(updates,existingMenu);
        System.out.println("\n\nAfter Menu"+existingMenu+"\n");

        return menuRepository.save(existingMenu);
    }

    private Boolean checkPermission(Long menuId, UserPrincipal currentUser){

        List<MMenu>  allAvailableMenu = this.getAllInHotel(currentUser.getHotel().getId());
        boolean isMenuIdPresent = allAvailableMenu.stream()
                .anyMatch(menu -> menu.getId().equals(menuId));
        if(!isMenuIdPresent){
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission ");
            throw new AccessDeniedException(apiResponse);
        }
        return  isMenuIdPresent;

    }

}
