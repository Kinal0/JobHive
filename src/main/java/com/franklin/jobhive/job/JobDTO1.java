package com.franklin.jobhive.job;


import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.skill.Skill;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JobDTO1 {
    private Long job_id;

    private String job_title;

    private String job_description;

    private String job_city;

    private String job_state;

    private String job_type;

    private Long company_id;

    private String[] skills;

}

