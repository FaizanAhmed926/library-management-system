package com.library.library_backend.mapper;

import com.library.library_backend.model.Fine;
import com.library.library_backend.payload.dto.FineDTO;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {
    public FineDTO toDTO(Fine fine){
        if (fine==null){
            return  null;
        }
        FineDTO dto=new FineDTO();
        dto.setId(fine.getId());

        if (fine.getBookLoan()!=null){
            dto.setBookLoanId(fine.getBookLoan().getId());
            if (fine.getBookLoan().getBook()!=null){
                dto.setBookIsbn(fine.getBookLoan().getBook().getIsbn());
                dto.setBookTitle(fine.getBookLoan().getBook().getTitle());
            }
        }

        if (fine.getUser()!=null){
            dto.setUserId(fine.getUser().getId());
            dto.setUserName(fine.getUser().getFullName());
            dto.setUserEmail(fine.getUser().getEmail());
        }

        dto.setType(fine.getType());
        dto.setAmount(fine.getAmount());
        dto.setStatus(fine.getStatus());
        dto.setReason(fine.getReason());
        dto.setNotes(fine.getNotes());

        if (fine.getWaivedBy()!=null){
            dto.setWaivedByUserId(fine.getWaivedBy().getId());
            dto.setWaivedByUserName(fine.getWaivedBy().getFullName());
        }
        dto.setWaivedAt(fine.getWaivedAt());
        dto.setWaivedByUserName(fine.getWaiverReason());

        dto.setPaidAt(fine.getPaidAt());
        if (fine.getProcessedBy()!=null){
            dto.setProcessedByUserId(fine.getProcessedBy().getId());
            dto.setProcessByUserName(fine.getProcessedBy().getFullName());
        }
        dto.setTransactionId(fine.getTransactionId());
        dto.setCreatedAt(fine.getCreatedAt());
        dto.setUpdatedAt(fine.getUpdatedAt());

        return  dto;
    }
}
