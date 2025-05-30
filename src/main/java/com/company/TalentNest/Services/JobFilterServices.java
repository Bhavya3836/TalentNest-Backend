package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Request.LocationRequest;
import com.company.TalentNest.DTO.Request.SalaryFilterRequest;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class JobFilterServices {

    @Autowired
    JobRepository jobRepository;

    public List<JobPost> searchByLocation(LocationRequest location){
        String locationExtraction  = location.getLocation();
        List<JobPost> job = jobRepository.findByLocation(locationExtraction);
        return job;
    }

    public List<JobPost> searchBySalary(SalaryFilterRequest salaryFilterRequest){
        List<JobPost> job = jobRepository.findBySalaryBetweenOrderBySalaryAsc(salaryFilterRequest.getStartingRange(),salaryFilterRequest.getEndingRange());
        return job;
    }
}
