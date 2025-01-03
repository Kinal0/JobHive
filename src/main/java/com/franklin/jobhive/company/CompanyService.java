package com.franklin.jobhive.company;

import com.franklin.jobhive.job.Job;
import com.franklin.jobhive.job.JobRepository;
import com.franklin.jobhive.skill.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyrepo;

    @Autowired
    JobRepository jobrepo;


    // CRUD service for Companies


    // Service method to create a company

    public Long createCompany(Company newCompany) {
        Company savedCompany = companyrepo.save(newCompany);
        return savedCompany.getCompany_Id();
    }

    // Service method to read all companies
    public List<Company> readCompanies() {
        List<Company> companyList = new ArrayList<>();
        companyList = companyrepo.findAll();
        return companyList;
    }

    public Page<Company> readCompanies(Pageable pageable) {
        return companyrepo.findAll(pageable);
    }
    // Service method to read company by ID
    public Optional<Company> readCompanyById(Long id) {
        Optional<Company> company = companyrepo.findById(id);
        return company;
    }

    public String companyNameById(Long companyId){
        return companyrepo.getCompanyNameById(companyId);
    }


    // Service method to update details of existing Company
    public String updateCompany(Company updatedCompany) {
        Company originalCompany = companyrepo.getReferenceById(updatedCompany.getCompany_Id());
        originalCompany.setCompany_name(updatedCompany.getCompany_name());
        originalCompany.setCompany_description(updatedCompany.getCompany_description());
        originalCompany.setCompany_state(updatedCompany.getCompany_state());
        originalCompany.setCompany_city(updatedCompany.getCompany_city());
        originalCompany.setCompany_address(updatedCompany.getCompany_address());
        originalCompany.setCompany_contact(updatedCompany.getCompany_contact());
        companyrepo.save(updatedCompany);
        return "Updated company details that has ID: "+originalCompany.getCompany_Id();
    }

    // Service method to delete company by ID
    public String deleteCompany(Long id) {
        companyrepo.deleteById(id);
        return "Deleted company with ID: " + id;
    }
    // Service method to check if company id is attached to a job listing
    public boolean companyIsActive(Long id) {
        Set<Long> companyIds = jobrepo.getActiveCompanies();
        return companyIds.contains(id);
    }

}
