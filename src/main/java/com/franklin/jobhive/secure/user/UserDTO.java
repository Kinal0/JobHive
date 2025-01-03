package com.franklin.jobhive.secure.user;

import com.franklin.jobhive.secure.role.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String user_name;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String securityQuestion1;
    private String securityAnswer1;
    private String securityQuestion2;
    private String securityAnswer2;
    private String[] skills;
    private Long role_id;

    public String getAllSkills(){
        StringBuilder sb = new StringBuilder();
        for (String skill : this.skills) {
            sb.append(skill).append("#");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String skillsString = sb.toString();

        return skillsString;
    }
}
