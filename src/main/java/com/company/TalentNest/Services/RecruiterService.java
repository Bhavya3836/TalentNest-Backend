package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Request.JobApplicationRecruiterActionRequest;
import com.company.TalentNest.DTO.Request.JobPostRequest;
import com.company.TalentNest.Model.*;
import com.company.TalentNest.Repo.*;
import com.company.TalentNest.Security.jwt.JwtTokenProvider;
import com.company.TalentNest.config.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;



import static com.company.TalentNest.Enums.ApplicationStatus.*;


@Service
public class RecruiterService {

    private final UserRepository userRepository;
    public final JobRepository jobRepository;
    public final JwtTokenProvider jwtTokenProvider;
    private final CompanyRepository companyRepository;
    private final SkillsRespository skillsRespository;
    private final ApplicationRepository applicationRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;


    public RecruiterService(UserRepository userRepository, JobRepository jobRepository, JwtTokenProvider jwtTokenProvider, CompanyRepository companyRepository, SkillsRespository skillsRespository, ApplicationRepository applicationRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.companyRepository = companyRepository;
        this.skillsRespository = skillsRespository;
        this.applicationRepository = applicationRepository;
        this.javaMailSender = javaMailSender;
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

    public Application rejectApplicant(JobApplicationRecruiterActionRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + request.getApplicationId()));

        application.setCurrentStatus(REJECTED);
        applicationRepository.save(application);

        User user = userRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + application.getCandidateId()));

        JobPost job = jobRepository.findById(application.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found with ID: " + application.getJobPostId()));

        emailService.sendRejectionEmail(user.getEmail(),user.getFullName(),job.getCompanyName());


        return application;
    }


    public Application shortListApplicant(JobApplicationRecruiterActionRequest request){
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found with Id" + request.getApplicationId()));
        application.setCurrentStatus(SHORTLISTED);
        applicationRepository.save(application);

        User user = userRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + application.getCandidateId()));

        JobPost job = jobRepository.findById(application.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found with ID: " + application.getJobPostId()));

        emailService.sendShortlistedEmail(user.getEmail(),user.getFullName(),job.getCompanyName());

        return application;
    }

    public Application hireApplicant(JobApplicationRecruiterActionRequest request){
        Application application = applicationRepository.findById(request.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Application not found with Id" + request.getApplicationId()));
        application.setCurrentStatus(HIRED);
        applicationRepository.save(application);

        User user = userRepository.findById(application.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + application.getCandidateId()));

        JobPost job = jobRepository.findById(application.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found with ID: " + application.getJobPostId()));

        emailService.sendHiredEmail(user.getEmail(),user.getFullName(),job.getCompanyName());

        return application;
    }
}
