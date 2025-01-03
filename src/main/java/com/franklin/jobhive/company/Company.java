package com.franklin.jobhive.company;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name= "Company")
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_Id;

    private String company_name;
    private String company_description;
    private String company_state;
    private String company_city;
    private String company_address;
    private String company_contact;

    public Company(String company_name, String company_description, String company_state,
                   String company_city, String company_address, String company_contact) {
        this.company_name = company_name;
        this.company_description = company_description;
        this.company_state = company_state;
        this.company_city = company_city;
        this.company_address = company_address;
        this.company_contact = company_contact;
    }


    // Getters and Setters

    public Long getCompany_Id() { return company_Id; }

    public void setCompany_Id(Long company_Id) { this.company_Id = company_Id; }

    public String getCompany_name() { return company_name; }

    public void setCompany_name(String company_name) { this.company_name = company_name; }

    public String getCompany_description() { return company_description; }

    public void setCompany_description(String company_description) {
        this.company_description = company_description;
    }

    public String getCompany_state() { return company_state; }

    public void setCompany_state(String company_state) { this.company_state = company_state; }

    public String getCompany_city() { return company_city; }

    public void setCompany_city(String company_city) { this.company_city = company_city; }

    public String getCompany_address() { return company_address; }

    public void setCompany_address(String company_address) { this.company_address = company_address; }

    public String getCompany_contact() { return company_contact; }

    public void setCompany_contact(String company_contact) { this.company_contact = company_contact; }
}
