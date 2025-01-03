package com.franklin.jobhive.application;

import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.secure.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
@AllArgsConstructor
public class ApplicationDTO {

    private Job job;
    private User user;
    private Date appliedDate;


}