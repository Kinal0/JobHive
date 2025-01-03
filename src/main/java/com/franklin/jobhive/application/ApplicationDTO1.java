package com.franklin.jobhive.application;

import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.skill.Skill;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data

public class ApplicationDTO1 {

    private Job job;
    private User user;
    private Date appliedDate = new Date();
    private String comments;
    private Date lastUpdated = new Date();
    private String status;
    private List<Skill> AllSkills;
    private String[] userSkills;
    private String[] jobSkills;
    private List<Skill> userskill;
    private List<Skill> jobskill;
    private Company company;

}