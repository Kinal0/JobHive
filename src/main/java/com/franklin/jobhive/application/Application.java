package com.franklin.jobhive.application;


import com.franklin.jobhive.secure.user.CustomUserDetailsService;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.secure.user.UserRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Entity
@Data
public class Application {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "application_id", nullable = false, updatable = false)

    Long application_id;

    private Long user_id;

    private Long job_id;

    private String jobSkills;

    private String userSkills;

    private String Comments;

    private String Status;

    private Date lastSeen;

    private Date application_date;




}