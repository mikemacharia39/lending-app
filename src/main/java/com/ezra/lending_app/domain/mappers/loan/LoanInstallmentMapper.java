package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanInstallmentDto;
import com.ezra.lending_app.domain.entities.LoanInstallment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LoanInstallmentMapper {
    public abstract LoanInstallmentDto dto(LoanInstallment loanInstallment);
}
