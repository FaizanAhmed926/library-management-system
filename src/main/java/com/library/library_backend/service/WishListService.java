package com.library.library_backend.service;

import com.library.library_backend.model.WishList;
import com.library.library_backend.payload.dto.WishListDTO;
import com.library.library_backend.payload.response.PageResponse;

public interface WishListService {
    WishListDTO addToWishlist(Long bookId,String notes) throws Exception;
    void removeFromWishlist(Long bookId) throws Exception;
    PageResponse<WishListDTO> getMyWishlist(int page, int size) throws Exception;
}
