package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Request.JobPostRequest;
import com.company.TalentNest.Model.Company;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Model.Skills;
import com.company.TalentNest.Model.User;
import com.company.TalentNest.Repo.CompanyRepository;
import com.company.TalentNest.Repo.JobRepository;
import com.company.TalentNest.Repo.SkillsRespository;
import com.company.TalentNest.Repo.UserRepository;
import com.company.TalentNest.Security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class RecruiterService {

    private final UserRepository userRepository;
    public final JobRepository jobRepository;
    public final JwtTokenProvider jwtTokenProvider;
    private final CompanyRepository companyRepository;
    private final SkillsRespository skillsRespository;


    public RecruiterService(UserRepository userRepository, JobRepository jobRepository, JwtTokenProvider jwtTokenProvider, CompanyRepository companyRepository, SkillsRespository skillsRespository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyRepository = companyRepository;
        this.skillsRespository = skillsRespository;
    }


    @Transactional
    public JobPost createJob(JobPostRequest jobPostRequest, String token){

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        Integer companyId = jwtTokenProvider.getCompanyIdFromToken(token);

        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (companyId == null || (user.getCompany() != null && !companyId.equals(user.getCompany().getId()))) {
            throw new RuntimeException("Invalid company ID");
        }



        Company com = user.getCompany();

        Optional<JobPost> existingJob = jobRepository.findByTitleAndLocationAndExperienceAndCompanyNameAndStatus(
                jobPostRequest.getTitle(),
                jobPostRequest.getLocation(),
                jobPostRequest.getExperience(),
                com.getName(),
                true
        );

        if (existingJob.isPresent()) {
            throw new IllegalArgumentException("An active job with the same title, skills, location, experience, and company already exists");
        }

        JobPost jobPost = new JobPost();
        jobPost.setTitle(jobPostRequest.getTitle());
        jobPost.setDescription(jobPostRequest.getDescription());
        jobPost.setCompanyName(com!=null ? com.getName() : "Unknown");
        jobPost.setLocation(jobPostRequest.getLocation());
        jobPost.setExperience(jobPostRequest.getExperience());
        jobPost.setCreatedAt(java.time.LocalDateTime.now());
        jobPost.setUpdatedAt(java.time.LocalDateTime.now());
        jobPost.setPostedBy(userId.intValue());
        jobPost.setStatus(true);

        if(jobPostRequest.getSkillIds() != null && !jobPostRequest.getSkillIds().isEmpty()){
            Set<Skills> skillsSet = new HashSet<>(skillsRespository.findAllById(jobPostRequest.getSkillIds()));
            jobPost.setSkillsRequired(skillsSet);
        }

        return jobRepository.save(jobPost);
    }

    public List<JobPost> getAllActiveJobs(){
        return jobRepository.findByStatusTrue();
    }

    public List<JobPost> getSpecificCompanyJobs(Integer id){
        Optional<Company> com = companyRepository.findById(Long.valueOf(id));
        String name = com.get().getName();
        return jobRepository.findByCompanyName(name);
    }

    public Optional<JobPost> changeStatus(Integer id){
        Optional<JobPost> jobPost = jobRepository.findById(id);

        if(jobPost.isPresent()){
            JobPost jobPost1 = jobPost.get();
            boolean currentStatus = jobPost1.isStatus();
            jobPost1.setStatus(!currentStatus);
            jobRepository.save(jobPost1);
            return Optional.of(jobPost1);
        }
        return Optional.empty();
    }
}
