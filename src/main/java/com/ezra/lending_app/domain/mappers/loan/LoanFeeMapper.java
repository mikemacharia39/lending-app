package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanFeeDto;
import com.ezra.lending_app.domain.entities.LoanFee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LoanFeeMapper {
    public abstract LoanFeeDto dto(LoanFee loanFee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateModified", ignore = true)
    @Mapping(target = "loan", ignore = true)
    public abstract LoanFee entity(LoanFeeDto loanFeeDto);
}
