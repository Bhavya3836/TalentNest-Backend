package com.company.TalentNest.Model;

import com.company.TalentNest.Enums.ApplicationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="applications", schema = "talent")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_post_id", nullable = false )
    private Integer jobPostId;

    @Column(name = "candidate_id", nullable = false )
    private Integer candidateId;

    @Column(name = "resume_id")
    private Integer resumeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private ApplicationStatus currentStatus;

    @Column(name= "applied_at")
    private LocalDateTime appliedAt;

    public Application(Integer jobPostId, Integer candidateId, Integer resumeId, ApplicationStatus currentStatus, LocalDateTime appliedAt) {
        this.jobPostId = jobPostId;
        this.candidateId = candidateId;
        this.resumeId = resumeId;
        this.currentStatus = currentStatus;
        this.appliedAt = appliedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public Integer getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getResumeId() {
        return resumeId;
    }

    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public ApplicationStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(ApplicationStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
