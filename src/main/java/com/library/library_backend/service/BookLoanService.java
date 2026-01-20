package com.library.library_backend.service;


import com.library.library_backend.domain.BookLoanStatus;
import com.library.library_backend.payload.dto.BookLoanDTO;
import com.library.library_backend.payload.request.BookLoanSearchRequest;
import com.library.library_backend.payload.request.CheckinRequest;
import com.library.library_backend.payload.request.CheckoutRequest;
import com.library.library_backend.payload.request.RenewalRequest;
import com.library.library_backend.payload.response.PageResponse;

public interface BookLoanService {
    BookLoanDTO checkoutBook(CheckoutRequest checkoutrequest) throws Exception;
    BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutrequest) throws Exception;
    BookLoanDTO checkinBook(CheckinRequest checkinrequest) throws Exception;
    BookLoanDTO renewcheckout(RenewalRequest renewalRequest) throws Exception;
    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status,int page, int size) throws Exception;
    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);
    int updateOverdueBookLoan();
}
