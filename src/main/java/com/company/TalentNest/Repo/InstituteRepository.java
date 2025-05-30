package com.company.TalentNest.Repo;

import com.company.TalentNest.Model.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends JpaRepository<Institute,Long> {
}
