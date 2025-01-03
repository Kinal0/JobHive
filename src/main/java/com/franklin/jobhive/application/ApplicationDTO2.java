package com.franklin.jobhive.application;

import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.job.JobRepository;
import com.franklin.jobhive.job.JobService;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.skill.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationDTO2 {


    private Long application_id;

    private Job job;

    private User user;

    private Company company;

    private Date lastSeen;

    private Date application_date;

    private List<Skill> jobSkills;

    private List<Skill> userSkills;

    private int matchingPercentage;

    private String Comments;

    private String Status;




}
