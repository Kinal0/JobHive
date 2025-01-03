package com.franklin.jobhive.skill;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Skill")
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skill_id;

    private String skill_title;
    private String skill_description;

    public Skill(String skill_title, String skill_description) {
        this.skill_title = skill_title;
        this.skill_description = skill_description;
    }

    // Getters and Setters

    public Long getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(Long skill_Id) {
        this.skill_id = skill_Id;
    }

    public String getSkill_title() {
        return skill_title;
    }

    public void setSkill_title(String skill_title) {
        this.skill_title = skill_title;
    }

    public String getSkill_description() {
        return skill_description;
    }

    public void setSkill_description(String skill_description) {
        this.skill_description = skill_description;
    }

}
