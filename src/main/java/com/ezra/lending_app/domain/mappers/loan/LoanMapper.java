package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanResponseDto;
import com.ezra.lending_app.domain.entities.Loan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LoanMapper {
    abstract LoanResponseDto dto(Loan loan);
}
