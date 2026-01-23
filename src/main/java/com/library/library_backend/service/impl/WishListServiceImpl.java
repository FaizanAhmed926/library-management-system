package com.library.library_backend.service.impl;

import com.library.library_backend.mapper.WishlistMapper;
import com.library.library_backend.model.Book;
import com.library.library_backend.model.User;
import com.library.library_backend.model.WishList;
import com.library.library_backend.payload.dto.WishListDTO;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.repository.BookRepository;
import com.library.library_backend.repository.WishListRepository;
import com.library.library_backend.service.UserService;
import com.library.library_backend.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final UserService userService;
    private  final BookRepository bookRepository;
    private  final WishlistMapper wishlistMapper;

    @Override
    public WishListDTO addToWishlist(Long bookId, String notes) throws Exception {
        User user=userService.getCurrentUser();
        Book book=bookRepository.findById(bookId)
                .orElseThrow(()->new  Exception("Book not Found"));
        if (wishListRepository.existsByUserIdAndBookId(user.getId(),bookId)){
            throw new Exception("Book is Already in your wishlist");
        }
        WishList wishList=new WishList();
        wishList.setUser(user);
        wishList.setBook(book);
        wishList.setNotes(notes);
        WishList saved=wishListRepository.save(wishList);
        return wishlistMapper.toDTO(saved);
    }

    @Override
    public void removeFromWishlist(Long bookId) throws Exception {
        User user=userService.getCurrentUser();
        WishList wishList=wishListRepository.findByUserIdAndBookId(user.getId(), bookId);
        if (wishList==null){
            throw new Exception("Book is not in your wishList");
        }
        wishListRepository.delete(wishList);
    }

    @Override
    public PageResponse<WishListDTO> getMyWishlist(int page, int size) throws Exception {
        Long userId=userService.getCurrentUser().getId();
        Pageable pageable= PageRequest.of(page,size, Sort.by("addedAt").descending());
        Page<WishList> wishListPage=wishListRepository.findByUserId(userId,pageable);
        return convertToResponse(wishListPage);
    }

    private PageResponse<WishListDTO> convertToResponse(Page<WishList> wishListPage) {
        List<WishListDTO> wishListDTOS=wishListPage.getContent()
                .stream()
                .map(wishlistMapper::toDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(
                wishListDTOS,
                wishListPage.getNumber(),
                wishListPage.getSize(),
                wishListPage.getTotalElements(),
                wishListPage.getTotalPages(),
                wishListPage.isLast(),
                wishListPage.isFirst(),
                wishListPage.isEmpty()

        );
    }
}
