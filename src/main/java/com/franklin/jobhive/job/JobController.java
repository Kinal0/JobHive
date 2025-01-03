package com.franklin.jobhive.job;



import com.franklin.jobhive.application.ApplicationRepository;
import com.franklin.jobhive.application.ApplicationService;
import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.company.CompanyService;
import com.franklin.jobhive.secure.user.CustomUserDetails;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.secure.user.UserRepository;
import com.franklin.jobhive.skill.Skill;
import com.franklin.jobhive.skill.SkillRepository;
import com.franklin.jobhive.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequestMapping("job")
@Controller
public class JobController {

    @Autowired
    JobService jobService;
    @Autowired
    CompanyService companyservice;
    @Autowired
    SkillService skillservice;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationService applicationService;

    @GetMapping("/readjobs")
    public String readjobs(Model model, String errorMessage) {
        List<Job> jobs = jobService.readJobs();
        List<JobDTO> jobdtos = new ArrayList<>();

        for(Job job: jobs){

            JobDTO jobdto = new JobDTO();
            // Standard Objects
            jobdto.setJob_id(job.getJob_id());
            jobdto.setJob_title(job.getJob_title());
            jobdto.setJob_description(job.getJob_description());
            jobdto.setJob_city(job.getJob_city());
            jobdto.setJob_state(job.getJob_state());
            jobdto.setJob_type(job.getJob_type());
            // ID to company_name conversion - for display

            Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
            jobdto.setCompany(company);

            // ID's to title conversion - for display

            String[] skillIds = job.getSkilllist().split("#");
            List<Skill> skills = new ArrayList<>();
            for(int i=0; i<skillIds.length; i++){

                Skill skill = skillservice.readSkillById(Long.parseLong(skillIds[i])).orElse(null);
                skills.add(skill);
            }
            jobdto.setSkills(skills);
            jobdtos.add(jobdto);
        }

        model.addAttribute("jobDTOS", jobdtos);
        return "job_home";
    }

    @PostMapping("/searchjobs")
    public String searchResultsJobs(@RequestParam String searchterm, String searchtype, Model model) {

        List<Job> jobs = jobService.readJobs();
        List<JobDTO> jobdtos = new ArrayList<>();

        for(Job job: jobs){
            JobDTO jobdto = new JobDTO();
            if(searchterm.equals("") || searchterm.equals(null) ){
                jobdto.setJob_id(job.getJob_id());
                jobdto.setJob_title(job.getJob_title());
                jobdto.setJob_description(job.getJob_description());
                jobdto.setJob_city(job.getJob_city());
                jobdto.setJob_state(job.getJob_state());
                jobdto.setJob_type(job.getJob_type());
                Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                jobdto.setCompany(company);
                jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                jobdtos.add(jobdto);
            }
            else if(searchtype.equals("skill")){
                if(skillservice.getSkillNamesByIds(job.getSkillIds()).stream().anyMatch(searchterm::equalsIgnoreCase)){
                    jobdto.setJob_id(job.getJob_id());
                    jobdto.setJob_title(job.getJob_title());
                    jobdto.setJob_description(job.getJob_description());
                    jobdto.setJob_city(job.getJob_city());
                    jobdto.setJob_state(job.getJob_state());
                    jobdto.setJob_type(job.getJob_type());
                    Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                    jobdto.setCompany(company);
                    jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_title")){
                if(job.getJob_title().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdto.setJob_id(job.getJob_id());
                    jobdto.setJob_title(job.getJob_title());
                    jobdto.setJob_description(job.getJob_description());
                    jobdto.setJob_city(job.getJob_city());
                    jobdto.setJob_state(job.getJob_state());
                    jobdto.setJob_type(job.getJob_type());
                    Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                    jobdto.setCompany(company);
                    jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                    jobdtos.add(jobdto);
                }
            }

            else if(searchtype.equals("job_type")){
                if(job.getJob_type().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdto.setJob_id(job.getJob_id());
                    jobdto.setJob_title(job.getJob_title());
                    jobdto.setJob_description(job.getJob_description());
                    jobdto.setJob_city(job.getJob_city());
                    jobdto.setJob_type(job.getJob_type());
                    jobdto.setJob_state(job.getJob_state());
                    Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                    jobdto.setCompany(company);
                    jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                    jobdtos.add(jobdto);
                }
            }

            else if(searchtype.equals("job_description")) {
                if (job.getJob_description().toLowerCase().contains(searchterm.toLowerCase())) {
                    jobdto.setJob_id(job.getJob_id());
                    jobdto.setJob_title(job.getJob_title());
                    jobdto.setJob_description(job.getJob_description());
                    jobdto.setJob_city(job.getJob_city());
                    jobdto.setJob_state(job.getJob_state());
                    jobdto.setJob_type(job.getJob_type());
                    Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                    jobdto.setCompany(company);
                    jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                    jobdtos.add(jobdto);
                }
            }
                else if(searchtype.equals("job_state")){
                    if(job.getJob_state().toLowerCase().contains(searchterm.toLowerCase()) ){
                        jobdto.setJob_id(job.getJob_id());
                        jobdto.setJob_title(job.getJob_title());
                        jobdto.setJob_description(job.getJob_description());
                        jobdto.setJob_city(job.getJob_city());
                        jobdto.setJob_state(job.getJob_state());
                        jobdto.setJob_type(job.getJob_type());
                        Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                        jobdto.setCompany(company);
                        jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                        jobdtos.add(jobdto);
                    }
                }
                else if(searchtype.equals("job_city")){
                    if(job.getJob_city().toLowerCase().contains(searchterm.toLowerCase()) ){
                        jobdto.setJob_id(job.getJob_id());
                        jobdto.setJob_title(job.getJob_title());
                        jobdto.setJob_description(job.getJob_description());
                        jobdto.setJob_city(job.getJob_city());
                        jobdto.setJob_state(job.getJob_state());
                        jobdto.setJob_type(job.getJob_type());
                        Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                        jobdto.setCompany(company);
                        jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                        jobdtos.add(jobdto);
                    }
                }
            else if(searchtype.equals("company")){
                if(  companyservice.companyNameById(job.getCompany_id()).toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdto.setJob_id(job.getJob_id());
                    jobdto.setJob_title(job.getJob_title());
                    jobdto.setJob_description(job.getJob_description());
                    jobdto.setJob_city(job.getJob_city());
                    jobdto.setJob_state(job.getJob_state());
                    jobdto.setJob_type(job.getJob_type());
                    Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
                    jobdto.setCompany(company);
                    jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
                    jobdtos.add(jobdto);
                }
            }


        }

        model.addAttribute("jobDTOS", jobdtos);
        return "job_home";
    }

    @GetMapping("/readjobsactive")
    public String readjobsUser(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);

        List<Job> jobs = jobService.readJobsNotApplied();
        List<JobDTO> jobdtos = new ArrayList<>();

        for(Job job: jobs){

            JobDTO jobdto = new JobDTO();
            // Standard Objects
            jobdto.setJob_id(job.getJob_id());
            jobdto.setJob_title(job.getJob_title());
            jobdto.setJob_description(job.getJob_description());
            jobdto.setJob_city(job.getJob_city());
            jobdto.setJob_state(job.getJob_state());
            jobdto.setJob_type(job.getJob_type());


            // ID to company_name conversion - for display

            Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
            jobdto.setCompany(company);

            // ID's to title conversion - for display

            String[] jobSkillIds = job.getSkilllist().split("#");
            String[] userSkillIds = user.getSkills().split("#");
            List<Long> jobSkillIdList = Arrays.stream(jobSkillIds).map(Long::parseLong).collect(Collectors.toList());
            List<Long> userSkillIdList = Arrays.stream(userSkillIds).map(Long::parseLong).collect(Collectors.toList());
            userSkillIdList.retainAll(jobSkillIdList);
            jobdto.setMatchingPercentage((int)((float)userSkillIdList.size()/jobSkillIdList.size()*100));
            jobdto.setApplied(applicationRepository.getJobCount(job.getJob_id()));



            List<Skill> skills = new ArrayList<>();
            for(int i=0; i<jobSkillIds.length; i++){

                Skill skill = skillservice.readSkillById(Long.parseLong(jobSkillIds[i])).orElse(null);
                skills.add(skill);
            }
            jobdto.setSkills(skills);
            jobdtos.add(jobdto);
        }

        model.addAttribute("jobDTOS", jobdtos);
        return "job_home_jobseeker";
    }

    @PostMapping("/searchjobsactive")
    public String searchResultsActiveJobs(@RequestParam String searchterm, String searchtype, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        List<Job> jobs = jobService.readJobsNotApplied();
        List<JobDTO> jobdtos = new ArrayList<>();

        for(Job job: jobs){
            JobDTO jobdto = new JobDTO();
            jobdto.setJob_id(job.getJob_id());
            jobdto.setJob_title(job.getJob_title());
            jobdto.setJob_description(job.getJob_description());
            jobdto.setJob_city(job.getJob_city());
            jobdto.setJob_state(job.getJob_state());
            jobdto.setJob_type(job.getJob_type());
            Company company = companyservice.readCompanyById(job.getCompany_id()).orElse(null);
            jobdto.setCompany(company);
            jobdto.setSkills(skillservice.getSkillObjectsByIds(job.getSkillIds()));
            String[] jobSkillIds = job.getSkilllist().split("#");
            String[] userSkillIds = user.getSkills().split("#");
            List<Long> jobSkillIdList = Arrays.stream(jobSkillIds).map(Long::parseLong).collect(Collectors.toList());
            List<Long> userSkillIdList = Arrays.stream(userSkillIds).map(Long::parseLong).collect(Collectors.toList());
            userSkillIdList.retainAll(jobSkillIdList);
            jobdto.setMatchingPercentage((int)((float)userSkillIdList.size()/jobSkillIdList.size()*100));
            if(searchterm.equals("") || searchterm.equals(null) ){
                jobdtos.add(jobdto);
            }
            else if(searchtype.equals("skill")){
                if(skillservice.getSkillNamesByIds(job.getSkillIds()).stream().anyMatch(searchterm::equalsIgnoreCase)){
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_title")){
                if(job.getJob_title().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_description")) {
                if (job.getJob_description().toLowerCase().contains(searchterm.toLowerCase())) {
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_state")){
                if(job.getJob_state().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_city")){
                if(job.getJob_city().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("job_type")){
                if(  job.getJob_type().toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdtos.add(jobdto);
                }
            }
            else if(searchtype.equals("company")){
                if(  companyservice.companyNameById(job.getCompany_id()).toLowerCase().contains(searchterm.toLowerCase()) ){
                    jobdtos.add(jobdto);
                }
            }


        }

        model.addAttribute("jobDTOS", jobdtos);
        return "job_home_jobseeker";
    }

    @GetMapping("/createjob")
    public String showCreateForm(Model model) {
        model.addAttribute("jobdto1", new JobDTO1());
        model.addAttribute("skilllist", skillservice.readSkills());
        model.addAttribute("companylist", companyservice.readCompanies());
        return "create_job";
    }

    @PostMapping("/createjob")
    public String createJob(@ModelAttribute JobDTO1 jobdto1, Model model) {

        String[] skilllist = jobdto1.getSkills();
        StringBuilder sb = new StringBuilder();
        for (String skill : skilllist) {
            sb.append(skill).append("#");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String skillsString = sb.toString();

        //System.out.println("\n\n"+"\n"+jobdto1.getJob_title()+"\n"+jobdto1.getJob_description()+"\n"+jobdto1.getJob_city()+"\n"+jobdto1.getJob_state()+"\n"+jobdto1.getCompany_id()+"\n"+skillsString+"\n\n\n");

        jobService.createJob(new Job(0L, jobdto1.getJob_title(), jobdto1.getJob_description(), jobdto1.getJob_city(), jobdto1.getJob_type(), jobdto1.getJob_state(), jobdto1.getCompany_id(), skillsString ));




        return "redirect:/job/readjobs";
    }

    @PostMapping("/updatejob1/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<Job> job = jobService.readJobById(id);
        if (job == null) {
            return "redirect:/job/readjobs"; // or return an error page
        }

        JobDTO1 jobdto1 = new JobDTO1();
        jobdto1.setJob_id(job.get().getJob_id());
        jobdto1.setJob_title(job.get().getJob_title());
        jobdto1.setJob_description(job.get().getJob_description());
        jobdto1.setJob_city(job.get().getJob_city());
        jobdto1.setJob_state(job.get().getJob_state());
        jobdto1.setJob_type(job.get().getJob_type());
        jobdto1.setCompany_id(job.get().getCompany_id());
        jobdto1.setSkills(job.get().getSkilllist().split("#"));

        model.addAttribute("jobdto1", jobdto1);
        model.addAttribute("skilllist", skillservice.readSkills());
        model.addAttribute("companylist", companyservice.readCompanies());
        return "update_job";
    }

    @PostMapping("/updatejob2")
    public String updateJob(@ModelAttribute JobDTO1 jobdto1, Model model) {

        String[] skilllist = jobdto1.getSkills();
        StringBuilder sb = new StringBuilder();
        for (String skill : skilllist) {
            sb.append(skill).append("#");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String skillsString = sb.toString();


        jobService.updateJob(new Job(jobdto1.getJob_id(), jobdto1.getJob_title(), jobdto1.getJob_description(), jobdto1.getJob_city(),jobdto1.getJob_type(), jobdto1.getJob_state(), jobdto1.getCompany_id(), skillsString));

        return "redirect:/job/readjobs";
    }



    @PostMapping("/deletejob/{id}")
    public String deleteJob(@PathVariable Long id, RedirectAttributes redirectAttributes){
        if(applicationService.getActiveJobs().contains(id)){
            redirectAttributes.addFlashAttribute("errorMessage", "The job cannot be deleted because there are applications associated with it.");
            return "redirect:/job/readjobs";
        }
        jobService.deleteJob(id);
        return "redirect:/job/readjobs";
    }

}