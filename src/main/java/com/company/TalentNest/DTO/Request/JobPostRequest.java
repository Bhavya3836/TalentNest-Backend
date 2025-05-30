package com.company.TalentNest.DTO.Request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostRequest {
    private String title;
    private String description;
    private String location;
    private BigDecimal experience;

    private List<Integer> skillIds;//This is a different TABLE
}
