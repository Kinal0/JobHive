package com.franklin.jobhive.secure.user;

import com.franklin.jobhive.secure.role.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long user_id;

    @Column(unique = true)
    private String user_name;
    private String first_name;
    private String last_name;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "security_question1")
    private String securityQuestion1;

    @Column(name = "security_answer1")
    private String securityAnswer1;

    @Column(name = "security_question2")
    private String securityQuestion2;

    @Column(name = "security_answer2")
    private String securityAnswer2;
    private String skills;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    public Long getUser_id() {
        return user_id;
    }

}
