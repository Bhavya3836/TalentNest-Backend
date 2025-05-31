package com.company.TalentNest.Repo;

import com.company.TalentNest.Model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application,Integer> {
    boolean existsByCandidateIdAndJobPostId(Integer candidateId, Integer jobPostId);

}
