package com.library.library_backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private  int pageSize;
    private  long totalElement;
    private  int totalPages;
    private  boolean last;
    private boolean first;
    private  boolean empty;
}
