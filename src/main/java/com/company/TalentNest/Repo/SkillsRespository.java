package com.company.TalentNest.Repo;

import com.company.TalentNest.Model.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRespository extends JpaRepository<Skills,Integer> {
}
