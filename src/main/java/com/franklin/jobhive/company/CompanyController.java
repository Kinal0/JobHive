package com.franklin.jobhive.company;

import com.franklin.jobhive.company.CompanyService;
import com.franklin.jobhive.job.JobRepository;
import com.franklin.jobhive.skill.Skill;
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

@RequestMapping("company")
@Controller
public class CompanyController {

    @Autowired
    CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/createcompanies")
    public String createCompanies(Model model) {
        Company company = new Company();
        model.addAttribute("company", company);
        return "company_create";
    }

    @PostMapping("/createcompanies")
    public String createCompanies(@ModelAttribute Company company, Model model){
        Long result = companyService.createCompany(company);
        return "redirect:/company/readcompanies";
    }

    @GetMapping("/readcompanies")
    public String readCompanies(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 6;
        Page<Company> companyPage = companyService.readCompanies(PageRequest.of(page, pageSize));
        model.addAttribute("companies", companyPage.getContent());
        model.addAttribute("currentPage", companyPage.getNumber());
        model.addAttribute("totalPages", companyPage.getTotalPages());
        return "company_home";
    }

    @GetMapping("/readcompanies/{id}")
    public ResponseEntity<Optional<Company>>readCompanyById(@PathVariable Long id) {
        Optional<Company> companyById = companyService.readCompanyById(id);
        return ResponseEntity.ok(companyById);
    }

    @PostMapping("/updateCompany")
    public String updateCompany(@RequestParam Map<String, String > body) {
        Company company = new Company();
        company.setCompany_Id(Long.parseLong(body.get("id")));
        company.setCompany_name(body.get("name"));
        company.setCompany_description(body.get("description"));
        company.setCompany_state(body.get("state"));
        company.setCompany_city(body.get("city"));
        company.setCompany_address(body.get("address"));
        company.setCompany_contact(body.get("contact"));
        String result = companyService.updateCompany(company);

        return "redirect:/company/readcompanies";
    }

    @GetMapping("/updateCompany/{id}")
    public String updateCompanyById(@PathVariable Long id, Model model) {
        Company company = companyService.readCompanyById(id).get();
        model.addAttribute("company", company);
        return "company_update";
    }
//
    @PostMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes){
        // Make sure the company doesn't have an active job listing. Otherwise, send an error message
        if (!companyService.companyIsActive(id)) {
            String result = companyService.deleteCompany(id);
            return "redirect:/company/readcompanies";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Company cannot be deleted because it has an active job listing.");
            return "redirect:/company/readcompanies";
        }
    }
}
