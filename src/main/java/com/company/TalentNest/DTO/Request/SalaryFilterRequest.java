package com.company.TalentNest.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class SalaryFilterRequest {
    private BigDecimal startingRange;
    private BigDecimal endingRange;
}
