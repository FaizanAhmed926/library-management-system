package com.library.library_backend.service;


import com.library.library_backend.domain.BookLoanStatus;
import com.library.library_backend.payload.dto.BookLoanDTO;
import com.library.library_backend.payload.request.BookLoanSearchRequest;
import com.library.library_backend.payload.request.Checkinrequest;
import com.library.library_backend.payload.request.Checkoutrequest;
import com.library.library_backend.payload.request.RenewalRequest;
import com.library.library_backend.payload.response.PageResponse;

public interface BookLoanService {
    BookLoanDTO checkoutBook(Checkoutrequest checkoutrequest) throws Exception;
    BookLoanDTO checkoutBookForUser(Long userId, Checkoutrequest checkoutrequest) throws Exception;
    BookLoanDTO checkinBook(Checkinrequest checkinrequest) throws Exception;
    BookLoanDTO renewcheckout(RenewalRequest renewalRequest) throws Exception;
    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status,int page, int size) throws Exception;
    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);
    int updateOverdueBookLoan();
}
