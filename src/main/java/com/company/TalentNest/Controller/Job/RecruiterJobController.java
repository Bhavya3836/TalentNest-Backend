package com.company.TalentNest.Controller.Job;

import com.company.TalentNest.DTO.Request.JobApplicationRecruiterActionRequest;
import com.company.TalentNest.DTO.Request.JobPostRequest;
import com.company.TalentNest.Model.Application;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Services.RecruiterService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@RestController
@RequestMapping("/admin/api/v1/jobs")
public class RecruiterJobController {
    private final RecruiterService jobService;
    private final RecruiterService recruiterService;


    public RecruiterJobController(RecruiterService jobService, RecruiterService recruiterService) {
        this.jobService = jobService;
        this.recruiterService = recruiterService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_RECRUITER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createJob(
            @RequestBody JobPostRequest jobPostRequest,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authorities: " + (authentication != null ? authentication.getAuthorities() : "null"));
            System.out.println("Principal: " + (authentication != null ? authentication.getPrincipal() : "null"));

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            JobPost job = jobService.createJob(jobPostRequest, token);
            return ResponseEntity.ok(job);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error in createJob: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating job: " + e.getMessage());
        }
    }


    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeActiveStatus(@PathVariable Integer id){
        try{
            Optional<JobPost> job = jobService.changeStatus(id);
            if(job!=null){return ResponseEntity.ok(job);}
            else throw new RuntimeException("JobID not found");

        } catch (Exception e) {
            System.out.println("Coundn't change the status");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching jobs: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ROLE_RECRUITER') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/reject")
    public ResponseEntity<?> rejectApplication(@RequestBody JobApplicationRecruiterActionRequest request) {
        try {
            Application application = recruiterService.rejectApplicant(request);
            return ResponseEntity.ok(application);
        } catch (IllegalArgumentException e) {
            System.out.println("Application ID not found: " + request.getApplicationId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application not found with ID: " + request.getApplicationId());
        } catch (Exception e) {
            System.out.println("Error rejecting application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_RECRUITER') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/shortlist")
    public ResponseEntity<?> shortListApplicant(@RequestBody JobApplicationRecruiterActionRequest request) {
        try {
            Application application = recruiterService.shortListApplicant(request);
            return ResponseEntity.ok(application);
        } catch (IllegalArgumentException e) {
            System.out.println("Application ID not found: " + request.getApplicationId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application not found with ID: " + request.getApplicationId());
        } catch (Exception e) {
            System.out.println("Error rejecting application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('ROLE_RECRUITER') or hasAuthority('ROLE_ADMIN')")
    @PutMapping("/hired")
    public ResponseEntity<?> hireApplicant(@RequestBody JobApplicationRecruiterActionRequest request) {
        try {
            Application application = recruiterService.hireApplicant(request);
            return ResponseEntity.ok(application);
        } catch (IllegalArgumentException e) {
            System.out.println("Application ID not found: " + request.getApplicationId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application not found with ID: " + request.getApplicationId());
        } catch (Exception e) {
            System.out.println("Error rejecting application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }





}