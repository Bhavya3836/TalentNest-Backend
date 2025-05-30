package com.company.TalentNest.Controller.Job;

import com.company.TalentNest.DTO.Custom.JobMatchChecker;
import com.company.TalentNest.DTO.Request.SalaryFilterRequest;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Services.JobFilterServices;
import com.company.TalentNest.Services.JobServices;
import com.company.TalentNest.Services.RecruiterService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.TalentNest.DTO.Request.LocationRequest;

import java.util.List;

@RestController
@RequestMapping("/user/api/v1/jobs")
@RequiredArgsConstructor
public class UserJobController {

    private final RecruiterService jobService;
    private final JobServices userJobServices;
    private final JobFilterServices jobFilterServices;

    @GetMapping
    public ResponseEntity<?> showListOfJob() {
        try {
            List<JobPost> jobs = jobService.getAllActiveJobs();
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            System.out.println("Error in showListOfJob: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching jobs: " + e.getMessage());
        }
    }

    @GetMapping("/specificCompany/{id}")
    public ResponseEntity<?> getSpecificCompanyJobs(@PathVariable Integer id) {
        try {
            List<JobPost> jobs = jobService.getSpecificCompanyJobs(id);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            System.out.println("Error in getSpecificCompanyJobs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching company jobs: " + e.getMessage());
        }
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<JobMatchChecker>> getRecommendedJobs(@RequestParam("userId") Integer userId) {
        List<JobMatchChecker> jobs = userJobServices.getRecommendedJobs(userId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/filter/location")
    public ResponseEntity<List<JobPost>> filterByLocation(@RequestBody LocationRequest location){
        List<JobPost> job = jobFilterServices.searchByLocation(location);
        if(job.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
        return ResponseEntity.ok(job);
    }

    @GetMapping("/filter/salary")
    public ResponseEntity<List<JobPost>> filterBySalary(@RequestBody SalaryFilterRequest salaryFilterRequest){
        try{
            List<JobPost> job = jobFilterServices.searchBySalary(salaryFilterRequest);
            if(job.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(job);
        }
        catch (Exception e){
            System.out.println("Error in Finding Salary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }


    }

}
