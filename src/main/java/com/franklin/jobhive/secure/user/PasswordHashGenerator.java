package com.franklin.jobhive.secure.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class PasswordHashGenerator {

    public static void main(String args[]){

        BCryptPasswordEncoder passwordhash = new BCryptPasswordEncoder();

        List<String> users = new ArrayList<>();
        users.add("jack");
        users.add("chris");
        users.add("kinal");
        users.add("recruiter");

        for(String user: users){
            System.out.println(user+": "+passwordhash.encode(user));
        }

    }
}
