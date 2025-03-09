package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanRepaymentReceiptDto;
import com.ezra.lending_app.domain.entities.LoanRepaymentReceipt;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LoanRepaymentReceiptMapper {
    public abstract LoanRepaymentReceiptDto dto(LoanRepaymentReceipt loanRepaymentReceipt);
}
