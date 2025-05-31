package com.company.TalentNest.Controller.Job;


import com.company.TalentNest.DTO.Request.JobApplicationRequest;
import com.company.TalentNest.Model.Application;
import com.company.TalentNest.Services.JobApplicationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/api/v1/jobs/apply")
public class UserJobApplicationController {

    private final JobApplicationService jobApplicationService;

    public UserJobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<?> applyToJobs(@RequestBody JobApplicationRequest jobApplicationRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            Application application = jobApplicationService.applyToJob(jobApplicationRequest , token);
            return ResponseEntity.ok(application);

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error in applying to the job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
