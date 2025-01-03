package com.franklin.jobhive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication(exclude = {RepositoryRestMvcAutoConfiguration.class})
public class JobHiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobHiveApplication.class, args);

    }

    @Bean(name = "securityQuestions")
    public List<String> getSecurityQuestions(){
        List<String> securityQuestions = new ArrayList<>();
        securityQuestions.add("What was the name of your first pet?");
        securityQuestions.add("What is your mother's maiden name?");
        securityQuestions.add("What was the make and model of your first car?");
        securityQuestions.add("What is the name of the town where you were born?");
        securityQuestions.add("What was your favorite subject in school?");
        securityQuestions.add("What is your favorite book?");
        securityQuestions.add("What was the name of your first school?");
        securityQuestions.add("What is your father's middle name?");
        securityQuestions.add("What is the name of your favorite childhood friend?");
        securityQuestions.add("What is your favorite movie?");
        securityQuestions.add("What was the name of the street you grew up on?");
        securityQuestions.add("What was your high school mascot?");
        securityQuestions.add("What is the name of your favorite teacher?");
        securityQuestions.add("What is your favorite food?");
        securityQuestions.add("What is the name of your first employer?");

        return securityQuestions;
    }
}


