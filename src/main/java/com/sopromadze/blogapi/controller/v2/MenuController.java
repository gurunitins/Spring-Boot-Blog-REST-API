package com.sopromadze.blogapi.controller.v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sopromadze.blogapi.model.v2.MItem;
import com.sopromadze.blogapi.model.v2.MMenu;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.v2.request.MenuUpdateDTO;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.v2.ItemService;
import com.sopromadze.blogapi.service.v2.MenuService;
import com.sopromadze.blogapi.utils.ObjectTransformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/v2/menu")
public class MenuController {

    private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Autowired
    private ItemService itemService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<MMenu>> getAllMenu(@CurrentUser UserPrincipal currentUser) {
        List<MMenu> hotel = menuService.getAllInHotel(currentUser.getHotel().getId());
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<MMenu>> getActiveMenu(@CurrentUser UserPrincipal currentUser) {
        List<MMenu> hotel = menuService.getActive(currentUser.getHotel().getId());
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }



// CRUD ON SINGLE

    @GetMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MMenu> getMenu(@PathVariable(value = "menuId") Long menuId, @CurrentUser UserPrincipal currentUser) {

        MMenu resp = menuService.get(menuId);
        return new ResponseEntity<MMenu>(resp, HttpStatus.CREATED);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MMenu> createMenu(@Valid @RequestBody MMenu menu, @CurrentUser UserPrincipal currentUser) {

        menu.setHotelId(currentUser.getHotel().getId());
        MMenu updatedMenu = menuService.create(menu, currentUser);

        return new ResponseEntity<>(updatedMenu, HttpStatus.CREATED);
    }

    @PutMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MMenu> getMenu(@PathVariable(value = "menuId") Long menuId, @Valid @RequestBody MMenu menu, @CurrentUser UserPrincipal currentUser) {
        menu.setId(menuId);
        MMenu resp = menuService.update(menu, currentUser);

        return new ResponseEntity<MMenu>(resp, HttpStatus.OK);
    }

    @PatchMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MMenu> patchMenu(@PathVariable Long menuId, @Valid @RequestBody Map<String, Object> menuUpdateDTO, @CurrentUser UserPrincipal currentUser) {
        MMenu updatedMenu = menuService.partialUpdate(menuId, menuUpdateDTO, currentUser);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteMenu(@PathVariable(value = "menuId") Long menuId, @CurrentUser UserPrincipal currentUser) {

        ApiResponse resp = menuService.delete(menuId, currentUser);

        return new ResponseEntity<ApiResponse>(resp, HttpStatus.OK);
    }


//	ITEMS

    @PostMapping("/{menuId}/item")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MItem> createItems(@PathVariable(value = "menuId") Long menuId, @Valid @RequestBody MItem item, @CurrentUser UserPrincipal currentUser) {

        MItem newitem = itemService.create(item, menuId);

        return new ResponseEntity<>(newitem, HttpStatus.OK);
    }


    @PostMapping("/{menuId}/multi-item")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<MItem>> createMultiItems(@PathVariable(value = "menuId") Long menuId, @Valid @RequestBody List<MItem> item, @CurrentUser UserPrincipal currentUser) {

        List<MItem> newitem = itemService.create(item, menuId);

        return new ResponseEntity<>(newitem, HttpStatus.OK);
    }


    @GetMapping("/{menuId}/item")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<List<MItem>> getItems(@PathVariable(value = "menuId") Long menuId) {

        List<MItem> newitem = itemService.getByMenuId( menuId);

        return new ResponseEntity<>(newitem, HttpStatus.CREATED);
    }

    @GetMapping("/{menuId}/item/{itemId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MItem> getSingleItem(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "itemId") Long itemId) {

        MItem newitem = itemService.get( menuId);

        return new ResponseEntity<>(newitem, HttpStatus.CREATED);
    }

    @PatchMapping("/{menuId}/item/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<MItem> updateItem(
            @PathVariable(value = "menuId") Long menuId,
            @PathVariable(value = "itemId") Long itemId,
            @Valid @RequestBody Map<String,Object> item) {

        MItem resp = itemService.partialUpdate(menuId,itemId,item);

        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @DeleteMapping("/{menuId}/item/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSS')")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable(value = "itemId") Long itemId) {

        ApiResponse resp = itemService.delete(itemId);
        return new ResponseEntity<ApiResponse>(resp, HttpStatus.OK);

    }


//	COPY

    @GetMapping("/{menuId}/copy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MMenu> copy(@PathVariable(value = "menuId") Long menuId, @CurrentUser UserPrincipal currentUser) {

        MMenu resp = menuService.copy(menuId, currentUser);

        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }


}
