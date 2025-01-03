package com.franklin.jobhive.application;

import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.company.CompanyService;
import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.job.JobService;
import com.franklin.jobhive.secure.user.CustomUserDetails;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.secure.user.UserRepository;
import com.franklin.jobhive.skill.Skill;
import com.franklin.jobhive.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("application")
public class ApplicationController {

    @Autowired
    ApplicationService applicationService;

    @Autowired
    JobService jobService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SkillService skillService;

    @Autowired
    CompanyService companyService;




    @PostMapping("/createapplication")
    public String createApplication(@RequestParam Long job_id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        Job job = jobService.readJobById(job_id).get();
        Company company = companyService.readCompanyById(job.getCompany_id()).get();
        ApplicationDTO1 applicationDTO = new ApplicationDTO1();
        applicationDTO.setJob(job);
        applicationDTO.setUser(user);
        applicationDTO.setCompany(company);
        applicationDTO.setJobSkills(job.getSkilllist().split("#"));
        applicationDTO.setUserSkills(user.getSkills().split("#"));
        applicationDTO.setAllSkills(skillService.readSkills());
        String[] jobSkillIds = job.getSkilllist().split("#");
        String[] userSkillIds = user.getSkills().split("#");
        List<Long> jobSkillIdList = Arrays.stream(jobSkillIds).map(Long::parseLong).collect(Collectors.toList());
        List<Long> userSkillIdList = Arrays.stream(userSkillIds).map(Long::parseLong).collect(Collectors.toList());
        applicationDTO.setJobskill(skillService.getSkillObjectsByIds(jobSkillIdList));
        applicationDTO.setUserskill(skillService.getSkillObjectsByIds(userSkillIdList));

        model.addAttribute("app", applicationDTO);
        return "job_apply";
    }


    @PostMapping("/saveapplication")
    public String createApplicationPost(@ModelAttribute ApplicationDTO1 applicationDTO1){

        System.out.println("Reached controller with job ID: "+applicationDTO1.getJob().getJob_id() );
        Long newApplicationId = applicationService.createApplication(applicationDTO1);
        System.out.println("Application created with ID: "+newApplicationId);
        return "redirect:/job/readjobsactive";

    }


    @GetMapping("/readallapplications")
    public String readApplicationsall(Model model){

        System.out.println("Reached application readall controller");
        List<ApplicationDTO2> appList = applicationService.readAllApplications();
        model.addAttribute("appList", appList);
        return "job_applications";
    }

    @GetMapping("/readapplicationsuser")
    public String readApplicationsUser(Model model) {

        List<ApplicationDTO2> appList = applicationService.readApplications();
        model.addAttribute("appList", appList);
        return "my_applications";

    }

    @PostMapping("/viewapplication")
    public String readApplicationsUser(@RequestParam Long application_id, Model model,Authentication authentication) {
        Application application = applicationService.readApplicationById(application_id);
        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("RECRUITER"));
        if(hasRecruiterRole){
            application.setLastSeen(new Date());
            if(application.getStatus().equals("Submitted")){
                application.setStatus("Viewed");
            }
        }
        applicationService.saveApplication(application);
        ApplicationDTO2 applicationDTO2 = applicationService.viewApplication(application_id);
        model.addAttribute("app", applicationDTO2);
        return "view_application";

    }

    @PostMapping("/modifyapplication")
    public String modifyApplication(@RequestParam Long applicationID, String status, String comments) {
        Application application = applicationService.readApplicationById(applicationID);
        application.setComments(comments);
        application.setStatus(status);
        applicationService.saveApplication(application);
        return "redirect:/application/readallapplications";
    }


}