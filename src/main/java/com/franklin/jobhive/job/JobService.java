package com.franklin.jobhive.job;


import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.job.JobRepository;
import com.franklin.jobhive.secure.user.CustomUserDetails;
import com.franklin.jobhive.secure.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    JobRepository jobrepo;

    @Autowired
    UserRepository userRepository;


    // CRUD service for Job


    // Service method to create a Job
    public String createJob(Job newJob) {
        Job savedJob = jobrepo.save(newJob);
        return "Created job with ID: " + savedJob.getJob_id();
    }

    // Service method to read all job
    public List<Job> readJobs(){
        List<Job> jobList = new ArrayList<>();
        jobList = jobrepo.findAll();
        return jobList;
    }

    public List<Job> readJobsNotApplied(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userId = userRepository.getUserByUsername(username).getUser_id();
        List<Job> jobList = new ArrayList<>();
        jobList = jobrepo.getActiveJobs(userId);
        return jobList;
    }

    // Service method to read job by ID
    public Optional<Job> readJobById(Long id) {
        Optional<Job> job = jobrepo.findById(id);
        return job;
    }

    public List<Long> getActiveSkills(){
        List<String> skilllist = jobrepo.getActiveSkillIds();
        Set<Long> uniqueIds = skilllist.stream()
                .flatMap(s -> Arrays.stream(s.split("#")))
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        List<Long> resultList = new ArrayList<>(uniqueIds);

        return resultList;



    }



    // Service method to update details of existing job
    public String updateJob(Job updatedJob) {
        Job originalJob = jobrepo.getReferenceById(updatedJob.getJob_id());
        originalJob.setJob_title(updatedJob.getJob_title());
        originalJob.setJob_description(updatedJob.getJob_description());
        jobrepo.save(updatedJob);
        return "Updated job details that has ID: "+originalJob.getJob_id();
    }


    // Service method to delete job by Id
    public String deleteJob(Long id) {
        jobrepo.deleteById(id);
        return "Deleted job with ID: " + id;
    }
}