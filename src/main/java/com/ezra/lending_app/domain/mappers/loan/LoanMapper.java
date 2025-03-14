package com.ezra.lending_app.domain.mappers.loan;

import com.ezra.lending_app.api.dto.loan.LoanResponseDto;
import com.ezra.lending_app.domain.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        uses = {LoanInstallmentMapper.class,
                LoanFeeMapper.class})
@Component
public abstract class LoanMapper {

    @Mapping(target = "customerCode", source = "customer.code")
    @Mapping(target = "productCode", source = "product.code")
    public abstract LoanResponseDto dto(Loan loan);
}
