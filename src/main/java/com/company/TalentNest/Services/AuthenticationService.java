package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Request.LoginRequest;
import com.company.TalentNest.DTO.Request.SignupRequest;
import com.company.TalentNest.DTO.Response.JWTResponse;
import com.company.TalentNest.Model.Company;
import com.company.TalentNest.Model.Institute;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Model.User;
import com.company.TalentNest.Repo.CompanyRepository;
import com.company.TalentNest.Repo.InstituteRepository;
import com.company.TalentNest.Repo.UserRepository;
import com.company.TalentNest.Security.CustomUserDetails;
import com.company.TalentNest.Security.jwt.JwtTokenProvider;
import com.company.TalentNest.Exception.ResourceNotFoundException;
import com.company.TalentNest.Exception.IllegalAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final InstituteRepository instituteRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public JWTResponse register(SignupRequest signupRequest){


        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new IllegalAccessException("Email Already Exists");
        }

        Institute institute = null;
        Company company = null;


        if (signupRequest.getInstituteId()!=null){
            institute = instituteRepository.findById(signupRequest.getInstituteId())
                    .orElseThrow(() ->new ResourceNotFoundException("Institue Not Found"));
        }

        if (signupRequest.getCompanyId()!=null){
            company = companyRepository.findById(signupRequest.getCompanyId())
                    .orElseThrow(() ->new ResourceNotFoundException("Company Not Found"));
        }

        User user = new User();
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setRole(signupRequest.getRole());
        user.setInstitute(institute);
        user.setCompany(company);

        User save = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));

        String token = jwtTokenProvider.generateToken(authentication);

        return new JWTResponse(token,user.getEmail(), user.getPassword());

    }

    public JWTResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        String token = jwtTokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new JWTResponse(token, email, user.getRole().name());
    }




}
