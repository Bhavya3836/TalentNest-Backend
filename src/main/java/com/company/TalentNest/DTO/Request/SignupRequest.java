package com.company.TalentNest.DTO.Request;
import com.company.TalentNest.Enums.UserRoles;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {
    private String fullName;
    private String email;
    private String password;
    private UserRoles role;

    private Long instituteId;
    private Long companyId;
}
