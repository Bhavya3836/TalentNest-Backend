package com.company.TalentNest.Repo;

import com.company.TalentNest.DTO.Custom.JobMatchChecker;
import com.company.TalentNest.DTO.Request.LocationRequest;
import com.company.TalentNest.Model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobPost, Integer> {
    Optional<JobPost> findByTitleAndLocationAndExperienceAndCompanyNameAndStatus(
            String title, String location, BigDecimal experience, String companyName, Boolean active
    );
    List<JobPost> findByStatusTrue();
    List<JobPost> findByCompanyName(String companyName);
    List<JobPost> findByLocation(String location);

    List<JobPost> findBySalaryBetweenOrderBySalaryAsc(BigDecimal salaryAfter, BigDecimal salaryBefore);
    @Query(value = """

            SELECT
                j.id,
                j.title,
                COUNT(*) AS match_count
            FROM
                talent.job_posts j
            JOIN
                talent.job_skills js ON j.id = js.job_id
            JOIN
                talent.user_skills us ON js.skill_id = us.skill_id
            WHERE
                us.user_id = :userId
            GROUP BY
                j.id, j.title
            ORDER BY
                match_count DESC""", nativeQuery = true)
    List<JobMatchChecker> findJobsByMatchingSkills(@Param("userId") Integer userId);

}