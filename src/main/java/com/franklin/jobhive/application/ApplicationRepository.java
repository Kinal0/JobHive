package com.franklin.jobhive.application;

import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.secure.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT A FROM Application A WHERE A.user_id = :user_id")
    public List<Application> getApplicationsByUserId(@Param("user_id") Long user_id);

    @Query("SELECT count(A.job_id) FROM Application A WHERE A.job_id = :job_id")
    public int getJobCount(@Param("job_id") Long job_id);

    @Query("SELECT A.job_id FROM Application A")
    public List<Long> getActiveJobIds();


}