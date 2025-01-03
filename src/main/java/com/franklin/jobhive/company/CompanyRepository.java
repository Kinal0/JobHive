package com.franklin.jobhive.company;

import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.skill.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{

    @Query("SELECT c.company_Id FROM Company c WHERE c.company_name like :companyName")
    List<Long> getCompanyIdsByName(@Param("companyName") Long companyName);

    @Query("SELECT c.company_name FROM Company c WHERE c.company_Id = :companyId")
    String getCompanyNameById(@Param("companyId") Long companyId);

    Page<Company> findAll(Pageable pageable);
}
