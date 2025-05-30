package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Custom.JobMatchChecker;
import com.company.TalentNest.Repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServices {


    @Autowired
    private JobRepository jobRepository;

    public List<JobMatchChecker> getRecommendedJobs(Integer userId) {
        return jobRepository.findJobsByMatchingSkills(userId);
    }

}
