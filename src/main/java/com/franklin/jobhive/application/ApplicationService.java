package com.franklin.jobhive.application;

import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.company.CompanyService;
import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.job.JobRepository;
import com.franklin.jobhive.secure.user.CustomUserDetails;
import com.franklin.jobhive.secure.user.CustomUserDetailsService;
import com.franklin.jobhive.secure.user.User;
import com.franklin.jobhive.secure.user.UserRepository;
import com.franklin.jobhive.skill.Skill;
import com.franklin.jobhive.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    SkillService skillService;

    @Autowired
    CompanyService companyService;

    public List<ApplicationDTO2> readAllApplications(){

        List<Application> applicationList =  applicationRepository.findAll();
        List<ApplicationDTO2> appList = new ArrayList<>();

        for(Application app: applicationList){

            Job job = jobRepository.findById(app.getJob_id()).get();
            User user  = userRepository.getUserById(app.getUser_id());
            Company company = companyService.readCompanyById(job.getCompany_id()).get();
            List<Long> userSkillIdList = Arrays.stream(app.getUserSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> jobSkillIdList = Arrays.stream(app.getJobSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
            List<Skill> jobSkillList = skillService.getSkillObjectsByIds(jobSkillIdList);
            List<Skill> userSkillList = skillService.getSkillObjectsByIds(userSkillIdList);

            userSkillIdList.retainAll(jobSkillIdList);
            int matchingPercentage = (int)((float)userSkillIdList.size()/jobSkillIdList.size()*100);
            appList.add(new ApplicationDTO2(app.application_id, job, user, company, app.getLastSeen(), app.getApplication_date(), jobSkillList,
                        userSkillList, matchingPercentage, app.getComments(), app.getStatus()));
        }
        return appList;
    }

    public List<ApplicationDTO2> readApplications(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        List<Application> applicationList =  applicationRepository.getApplicationsByUserId(user.getUser_id());
        List<ApplicationDTO2> appList = new ArrayList<>();

        for(Application app: applicationList){

            Job job = jobRepository.findById(app.getJob_id()).get();
            List<Long> userSkillIdList = Arrays.stream(app.getUserSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
            List<Long> jobSkillIdList = Arrays.stream(app.getJobSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
            List<Skill> jobSkillList = skillService.getSkillObjectsByIds(jobSkillIdList);
            List<Skill> userSkillList = skillService.getSkillObjectsByIds(userSkillIdList);
            Company company = companyService.readCompanyById(job.getCompany_id()).get();
            userSkillIdList.retainAll(jobSkillIdList);
            int matchingPercentage = (int)((float)userSkillIdList.size()/jobSkillIdList.size()*100);
            appList.add(new ApplicationDTO2(app.application_id, job, user, company, app.getLastSeen(), app.getApplication_date(), jobSkillList,
                    userSkillList, matchingPercentage, app.getComments(), app.getStatus()));
        }
        return appList;

    }

    public Application readApplicationById(Long applicationId){
        return applicationRepository.findById(applicationId).get();
    }

    public Long createApplication(ApplicationDTO1 applicationDTO1){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user  = userRepository.getUserByUsername(username);
        Application newApplication = new Application();
        newApplication.setJob_id(applicationDTO1.getJob().getJob_id());
        newApplication.setUser_id(applicationDTO1.getUser().getUser_id());
        newApplication.setApplication_date(new Date());
        newApplication.setComments("");
        newApplication.setStatus("Submitted");
        newApplication.setJobSkills(String.join("#", applicationDTO1.getJobSkills()));
        newApplication.setUserSkills(String.join("#", applicationDTO1.getUserSkills()));
        Application savedApplication = applicationRepository.save(newApplication);
        return savedApplication.getApplication_id();
    }

    public String deleteApplication(Long applicationId){
        applicationRepository.deleteById(applicationId);
        return "Application with ID "+applicationId+" deleted";
    }

    public List<Long> getActiveJobs(){
        return applicationRepository.getActiveJobIds();
    }

    public ApplicationDTO2 viewApplication(Long applicationID) {
        Application app = applicationRepository.findById(applicationID).get();
        Job job = jobRepository.findById(app.getJob_id()).get();
        User user  = userRepository.getUserById(app.getUser_id());
        Company company = companyService.readCompanyById(job.getCompany_id()).get();
        List<Long> userSkillIdList = Arrays.stream(app.getUserSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
        List<Long> jobSkillIdList = Arrays.stream(app.getJobSkills().split("#")).map(Long::parseLong).collect(Collectors.toList());
        List<Skill> jobSkillList = skillService.getSkillObjectsByIds(jobSkillIdList);
        List<Skill> userSkillList = skillService.getSkillObjectsByIds(userSkillIdList);

        userSkillIdList.retainAll(jobSkillIdList);
        int matchingPercentage = (int)((float)userSkillIdList.size()/jobSkillIdList.size()*100);
        return new ApplicationDTO2(app.application_id, job, user, company, app.getLastSeen(), app.getApplication_date(), jobSkillList,
                userSkillList, matchingPercentage, app.getComments(), app.getStatus());
    }

    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }
}