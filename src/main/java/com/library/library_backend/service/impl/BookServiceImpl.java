package com.library.library_backend.service.impl;

import com.library.library_backend.exception.BookException;
import com.library.library_backend.mapper.BookMapper;
import com.library.library_backend.model.Book;
import com.library.library_backend.payload.dto.BookDTO;
import com.library.library_backend.payload.request.BookSearchRequest;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.repository.Bookrepository;
import com.library.library_backend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private  final Bookrepository bookrepository;
    private final BookMapper bookMapper;
    @Override
    public BookDTO createBook(BookDTO bookDTO) throws BookException {
        if(bookrepository.existsByIsbn(bookDTO.getIsbn())){
            throw  new BookException("Book with isbn"+bookDTO.getIsbn()+"already Exist");
        }
        Book book=bookMapper.toEntity(bookDTO);
        book.isAvailableCopiesValid();
        Book savedBook=bookrepository.save(book);

        return bookMapper.toDTO(savedBook);
    }

    @Override
    public List<BookDTO> createBooksBulk(List<BookDTO> bookDTOS) throws BookException {
        List<BookDTO> createdBooks=new ArrayList<>();
        for (BookDTO bookDTO:bookDTOS){
            BookDTO book=createBook(bookDTO);
            createdBooks.add(book);
        }
        return createdBooks;
    }

    @Override
    public BookDTO getBookById(Long bookId) throws BookException {
        Book book=bookrepository.findById(bookId)
                .orElseThrow(()->new BookException("Book not Found!"));
        return  bookMapper.toDTO(book);
    }

    @Override
    public BookDTO getBookByISBN(String isbn) throws BookException {
        Book book=bookrepository.findByIsbn(isbn)
                .orElseThrow(()->new BookException("Book not Found!"));
        return  bookMapper.toDTO(book);
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) throws BookException {
        Book existingBook=bookrepository.findById(bookId).orElseThrow(
                ()->new BookException("Book not Found!")
        );
        bookMapper.updateEntityFromDTO(bookDTO,existingBook);
        existingBook.isAvailableCopiesValid();
        Book savedBook=bookrepository.save(existingBook);
        return  bookMapper.toDTO(savedBook);
    }

    @Override
    public void deleteBook(Long bookId) throws BookException {
        Book existingBook=bookrepository.findById(bookId).orElseThrow(
                ()->new BookException("Book not Found!")
        );
        existingBook.setActive(false);
        bookrepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long bookId) throws BookException {
        Book existingBook=bookrepository.findById(bookId).orElseThrow(
                ()->new BookException("Book not Found!")
        );
        bookrepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest searchRequest) {
        Pageable pageable=createPageable(searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection());
        Page<Book> bookPage=bookrepository.searchBooksWithFilters(
                searchRequest.getSearchTerm(),
                searchRequest.getGenreId(),
                searchRequest.getAvailableOnly(),
                pageable
        );
        return  convertToPageResponse(bookPage);
    }

    @Override
    public long getTotalActiveBooks() {
        return bookrepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookrepository.countAvailableBooks();
    }

    private Pageable createPageable(int page,int size,String sortBy,String sortDirection){
        size=Math.min(size,10);
        size=Math.max(size,1);

        Sort sort=sortDirection.equalsIgnoreCase("ASC")
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);
    }

    private PageResponse<BookDTO> convertToPageResponse(Page<Book> books){
        List<BookDTO> bookDTOS=books.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return new PageResponse<>(bookDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty());
    }
}
