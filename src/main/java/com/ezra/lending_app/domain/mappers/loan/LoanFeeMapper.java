package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanFeeDto;
import com.ezra.lending_app.domain.entities.LoanFee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LoanFeeMapper {
    public abstract LoanFeeDto dto(LoanFee loanFee);
}
