package com.company.TalentNest.Services;

import com.company.TalentNest.DTO.Request.JobApplicationRequest;
import com.company.TalentNest.Model.Application;
import com.company.TalentNest.Model.JobPost;
import com.company.TalentNest.Model.Resume;
import com.company.TalentNest.Model.User;
import com.company.TalentNest.Repo.ApplicationRepository;
import com.company.TalentNest.Repo.JobRepository;
import com.company.TalentNest.Repo.ResumeRepository;
import com.company.TalentNest.Repo.UserRepository;
import com.company.TalentNest.Security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.company.TalentNest.Enums.ApplicationStatus.Applied;
@Getter
@Setter
@Service
public class JobApplicationService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResumeRepository resumeRepository;
    private final ApplicationRepository applicationRepository;

    public JobApplicationService(JobRepository jobRepository, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, ResumeRepository resumeRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.resumeRepository = resumeRepository;
        this.applicationRepository = applicationRepository;
    }


    public Application applyToJob(JobApplicationRequest jobApplicationRequest, String token){
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        User user = userRepository.findById(userId.intValue()).orElseThrow(() -> new RuntimeException("User Not found"));
        Optional<JobPost> job = jobRepository.findById(jobApplicationRequest.getJobPostId());
        if(!job.isPresent()){
            throw new IllegalArgumentException("Job Not found");
        }


        boolean alreadyApplied = applicationRepository.existsByCandidateIdAndJobPostId(user.getId(),job.get().getJobId());
        if(alreadyApplied){ throw new IllegalArgumentException("Already Applied");}

        Optional<Resume> resume = resumeRepository.findByUserId(user.getId());

        Application application = new Application();

        if(!resume.isEmpty()){

            application.setJobPostId(job.get().getJobId());
            application.setCandidateId(Math.toIntExact(userId));
            application.setResumeId(resume.get().getId());
            application.setCurrentStatus(Applied);
            application.setAppliedAt(java.time.LocalDateTime.now());

            applicationRepository.save(application);


        }else{
            throw new RuntimeException("Resume not found for Id");
        }

        return application;


    }


    public Optional<List<Application>> applicationHistory(String token){
        Long user = jwtTokenProvider.getUserIdFromToken(token);
        List<Application> applicationList = applicationRepository.findAllByCandidateId(Math.toIntExact(user));
        if(applicationList.isEmpty()){
            throw new IllegalArgumentException("User id does not have any applications");
        }

        return Optional.of(applicationList);
    }

}
