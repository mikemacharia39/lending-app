package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanResponseDto;
import com.ezra.lending_app.domain.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {LoanInstallmentMapper.class,
                LoanFeeMapper.class})
public abstract class LoanMapper {

    @Mapping(target = "customerCode", source = "customer.code")
    @Mapping(target = "productCode", source = "product.code")
    public abstract LoanResponseDto dto(Loan loan);
}
