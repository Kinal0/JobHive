package com.franklin.jobhive.job;

import com.franklin.jobhive.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE j.job_id NOT IN (SELECT a.job_id FROM Application a WHERE a.user_id = :userId)")
    List<Job> getActiveJobs(@Param("userId") Long userId);

    @Query("SELECT company_id FROM Job")
    Set<Long> getActiveCompanies();

    @Query("SELECT J.skilllist FROM Job J")
    public List<String> getActiveSkillIds();
}