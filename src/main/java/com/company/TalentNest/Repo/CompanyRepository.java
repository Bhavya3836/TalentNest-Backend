package com.company.TalentNest.Repo;

import com.company.TalentNest.Model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    @Repository
    public interface CompanyRepository extends JpaRepository<Company,Long>{

    }

