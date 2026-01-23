package com.library.library_backend.mapper;

import com.library.library_backend.model.WishList;
import com.library.library_backend.payload.dto.WishListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WishlistMapper {
    private final BookMapper bookMapper;
    public WishListDTO toDTO(WishList wishList){
        if (wishList==null){
            return null;
        }
        WishListDTO dto=new WishListDTO();
        dto.setId(wishList.getId());
        if (wishList.getUser()!=null){
            dto.setUserId(wishList.getUser().getId());
            dto.setUserFullName(wishList.getUser().getFullName());
        }
        if (wishList.getBook()!=null){
            dto.setBook(bookMapper.toDTO(wishList.getBook()));
        }
        dto.setAddedAt(wishList.getAddedAt());
        dto.setNotes(wishList.getNotes());
        return  dto;
    }
}
