package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanRepaymentRequestDto;
import com.ezra.lending_app.api.dto.loan.LoanRepaymentResponseDto;
import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LoanRepaymentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    @Mapping(target = "loan", ignore = true)
    @Mapping(target = "code", ignore = true)
    public abstract LoanRepaymentReceipt toEntity(LoanRepaymentRequestDto requestDto);

    public abstract LoanRepaymentResponseDto toDto(LoanRepaymentReceipt entity);
}
