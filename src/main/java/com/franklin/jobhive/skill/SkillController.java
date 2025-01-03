package com.franklin.jobhive.skill;

import com.franklin.jobhive.job.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("skill")
@Controller
public class SkillController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private JobService jobService;


    @GetMapping("/createskills")
    public String createSkills(Model model) {

        return "skill_create";
    }

    @PostMapping("/createskills")
    public String createSkills(@RequestParam String title, String description ) {
        Skill skill = new Skill();
        skill.setSkill_id(0L);
        skill.setSkill_description(description);
        skill.setSkill_title(title);
        Long result = skillService.createSkill(skill);

        return "redirect:/skill/readskills";
    }

    @GetMapping("/readskills")
    public String readSkills(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Skill> skillPage = skillService.readSkills(pageable);
    model.addAttribute("skills", skillPage.getContent());
    model.addAttribute("currentPage", skillPage.getNumber());
    model.addAttribute("totalPages", skillPage.getTotalPages());
    return "skill_home";
}


    @GetMapping("/updateSkill/{id}")
    public String updateSkill(@PathVariable Long id, Model model) {
        Skill skill = skillService.readSkillById(id).get();
        model.addAttribute("skill", skill);
        return "skill_update";
    }

    @GetMapping("/readskills/{id}")
    public ResponseEntity<Optional<Skill>> readSkillById(@PathVariable Long id) {
        Optional<Skill> skillById = skillService.readSkillById(id);
        return ResponseEntity.ok(skillById);
    }

    // end point to update skill
    @PostMapping("/updateSkill")
    public String updateSkill(@RequestParam Map<String, String> body){
        Skill skill = new Skill();
        skill.setSkill_id(Long.parseLong(body.get("id")));
        skill.setSkill_description(body.get("description"));
        skill.setSkill_title(body.get("title"));
        String result = skillService.updateSkill(skill);

        return "redirect:/skill/readskills";
    }

    @PostMapping("/deleteSkill/{id}")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes){
        if(jobService.getActiveSkills().contains(id)){
            redirectAttributes.addFlashAttribute("errorMessage", "The Skill cannot be deleted because there are jobs associated with it.");
            return "redirect:/skill/readskills";
        }
        skillService.deleteSkill(id);
        return "redirect:/skill/readskills";
    }



}
