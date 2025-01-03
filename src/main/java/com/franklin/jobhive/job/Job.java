package com.franklin.jobhive.job;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor

public class Job {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long job_id;

   private String job_title;

   private String job_description;

   private String job_city;

   private String job_type;

   private String job_state;

   private Long company_id;

   private String skilllist;

   public Job() {
   }

   public List<Long> getSkillIds(){

      if(this.skilllist.length()==0){
         return new ArrayList<Long>();
      }
      List<Long> ids = new ArrayList<>();
      String[] skillids = this.skilllist.split("#");
      for(String skill: skillids){
         ids.add(Long.parseLong(skill));
      }
      return ids;
   }

}
