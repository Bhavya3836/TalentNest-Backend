package com.company.TalentNest.Controller.User;

import com.company.TalentNest.DTO.Request.LoginRequest;
import com.company.TalentNest.DTO.Request.SignupRequest;
import com.company.TalentNest.DTO.Response.JWTResponse;
import com.company.TalentNest.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.company.TalentNest.Services.AuthenticationService;
import com.company.TalentNest.Exception.ResourceNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<JWTResponse> registerUser(@RequestBody SignupRequest signupRequest){
        try{
            JWTResponse response = authenticationService.register(signupRequest);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JWTResponse(null, null, null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JWTResponse(null,null,null));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            JWTResponse response = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JWTResponse(null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JWTResponse(null, null, null));
        }
    }


}
