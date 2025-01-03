package com.franklin.jobhive;

import com.franklin.jobhive.company.CompanyService;
import com.franklin.jobhive.skill.Skill;
import com.franklin.jobhive.skill.SkillService;
import com.franklin.jobhive.skill.SkillRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
public class SkillJunitTests {

    @Autowired
    private SkillService skillService;

    @MockBean
    private SkillRepository skillRepository;
    @Autowired
    private CompanyService companyService;

    @Test
    public void testCreateSkill_Success() {

        Skill skill = new Skill();
        skill.setSkill_title("Test_Title");
        skill.setSkill_description("Test_Description");

        Mockito.when(skillRepository.save(skill)).thenReturn(skill);

        Long skill_id =  skillService.createSkill(skill);

        assertEquals(skill_id, skill.getSkill_id());
        Mockito.verify(skillRepository, Mockito.times(1)).save(skill);

    }

    @Test
    public void testReadSkills() {
        List<Skill> expectedSkills = new ArrayList<>();
        expectedSkills.add(new Skill(1L, "Java", "Programming Language"));
        expectedSkills.add(new Skill(2L, "Spring Boot", "Java-based framework"));
        expectedSkills.add(new Skill(3L, "JavaScript", "Scripting language for web development"));
        expectedSkills.add(new Skill(4L, "SQL", "Structured Query Language"));
        expectedSkills.add(new Skill(5L, "Python", "High-level programming language"));

        Mockito.when(skillRepository.findAll()).thenReturn(expectedSkills);

        List<Skill> actual_skills = skillService.readSkills();

        assertEquals(expectedSkills.size(), actual_skills.size());
        for(int i = 0; i < expectedSkills.size(); i++) {
            assertEquals(expectedSkills.get(i), actual_skills.get(i));
        }
    }

    @Test
    public void testReadCompanyByID_Success() {
        Long skill_id = 1L;
        Skill expectedSkill = new Skill(1L, "Java", "Programming Language");

        Mockito.when(skillRepository.findById(skill_id)).thenReturn(Optional.of(expectedSkill));

        Optional<Skill> actual_skill = skillService.readSkillById(skill_id);
        assertTrue(actual_skill.isPresent());
        assertEquals(expectedSkill, actual_skill.get());
    }

    @Test
    public void testReadCompanyByID_Failure() {

        Long skill_id = 1L;

        Mockito.when(skillRepository.findById(skill_id)).thenReturn(Optional.empty());
        Optional<Skill> actual_skill = skillService.readSkillById(skill_id);
        assertFalse(actual_skill.isPresent());
    }

    @Test
    public void testUpdateSkill_Success() {
        Long existing_skill_id = 1L;
        Skill existing_skill = new Skill(1L, "Java", "Programming Language");
        Skill updated_skill = new Skill(1L, "Coffee", "Coffee-based framework");

        Mockito.when(skillRepository.getReferenceById(existing_skill_id)).thenReturn(existing_skill);
        Mockito.when(skillRepository.save(updated_skill)).thenReturn(updated_skill);

        skillService.updateSkill(updated_skill);

        assertEquals(existing_skill.getSkill_title(), updated_skill.getSkill_title());
        assertEquals(existing_skill.getSkill_description(), updated_skill.getSkill_description());
    }

    @Test
    public void testDeleteSkill() {
        Skill test_skill = new Skill(10L, "Test", " Test Programming Language");

        Mockito.when(skillRepository.save(test_skill)).thenReturn(test_skill);
        Mockito.when(skillRepository.findById(test_skill.getSkill_id())).thenReturn(Optional.of(test_skill));

        Long created_skill_id = skillService.createSkill(test_skill);
        assertEquals(created_skill_id, test_skill.getSkill_id());

        Optional<Skill> created_skill = skillService.readSkillById(created_skill_id);
        assertTrue(created_skill.isPresent());

        Mockito.doNothing().when(skillRepository).deleteById(10L);
        String deleted = skillService.deleteSkill(created_skill_id);
        assertTrue(deleted.contains(created_skill_id.toString()));

        Mockito.when(skillRepository.findById(created_skill_id)).thenReturn(Optional.empty());
        Optional<Skill> deleted_skill = skillService.readSkillById(created_skill_id);
        assertFalse(deleted_skill.isPresent());


    }

}
