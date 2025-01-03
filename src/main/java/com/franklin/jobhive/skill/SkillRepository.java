package com.franklin.jobhive.skill;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Page<Skill> findAll(Pageable pageable);
    @Query("SELECT s.skill_title FROM Skill s WHERE s.skill_id in :skillids")
    List<String> getSkillNamesByIds(@Param("skillids") List<Long> skillids);
}
