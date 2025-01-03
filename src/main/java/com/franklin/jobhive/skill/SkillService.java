package com.franklin.jobhive.skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SkillService {

    @Autowired
    SkillRepository skillrepo;


    // CRUD service for Skills


    // Service method to create a skill
    public Long createSkill(Skill newSkill) {
        Skill savedSkill = skillrepo.save(newSkill);
        return savedSkill.getSkill_id();
    }

    // Service method to read all skill
    public List<Skill> readSkills(){
        List<Skill> skillList = new ArrayList<>();
        skillList = skillrepo.findAll();
        return skillList;
    }

    public Page<Skill> readSkills(Pageable pageable) {
        return skillrepo.findAll(pageable);
    }

    // Service method to read skill by ID
    public Optional<Skill> readSkillById(Long id) {
        Optional<Skill> skill = skillrepo.findById(id);
        return skill;
    }

    public List<String> getSkillNamesByIds(List<Long> skillids){
        return skillrepo.getSkillNamesByIds(skillids);
    }

    public List<Skill> getSkillObjectsByIds(List<Long> skillids){
        return skillrepo.findAllById(skillids);
    }


    // Service method to update details of existing skill
    public String updateSkill(Skill updatedSkill) {
        Skill originalSkill = skillrepo.getReferenceById(updatedSkill.getSkill_id());
        originalSkill.setSkill_title(updatedSkill.getSkill_title());
        originalSkill.setSkill_description(updatedSkill.getSkill_description());
        skillrepo.save(updatedSkill);
        return "Updated skill details that has ID: "+originalSkill.getSkill_id();
    }


    // Service method to delete skill by Id
    public String deleteSkill(Long id) {
        skillrepo.deleteById(id);
        return "Deleted skill with ID: " + id;
    }
}
