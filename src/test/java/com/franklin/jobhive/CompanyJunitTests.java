package com.franklin.jobhive;

import com.franklin.jobhive.company.Company;
import com.franklin.jobhive.company.CompanyRepository;
import com.franklin.jobhive.company.CompanyService;
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
public class CompanyJunitTests {


    @Autowired
    private CompanyService companyService;

    @MockBean
    private CompanyRepository companyRepository;

    @Test
    public void testCreateCompany_Success() {
        Company newCompany = new Company("Test Company", "Test description", "CA", "Los Angeles", "123 Main St", "123-456-7890");
        Mockito.when(companyRepository.save(newCompany)).thenReturn(newCompany);

        Long companyId = companyService.createCompany(newCompany);

        assertEquals(newCompany.getCompany_Id(), companyId);
        Mockito.verify(companyRepository, Mockito.times(1)).save(newCompany);
    }


    @Test
    public void testReadCompanies() {
        List<Company> expectedCompanies = new ArrayList<>();
        expectedCompanies.add(new Company(1L, "Company A", "Description A", "TX", "Austin", "456 Elm St", "987-654-3210"));
        expectedCompanies.add(new Company(2L, "Company B", "Description B", "CA", "San Francisco", "789 Oak St", "555-123-4567"));

        Mockito.when(companyRepository.findAll()).thenReturn(expectedCompanies);

        List<Company> actualCompanies = companyService.readCompanies();

        assertEquals(expectedCompanies.size(), actualCompanies.size());
        for (int i = 0; i < expectedCompanies.size(); i++) {
            assertEquals(expectedCompanies.get(i), actualCompanies.get(i));
        }
    }

    @Test
    public void testReadCompanyById_Success() {
        Long companyId = 1L;
        Company expectedCompany = new Company(companyId, "Company A", "Description A", "TX", "Austin", "456 Elm St", "987-654-3210");

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.of(expectedCompany));

        Optional<Company> actualCompany = companyService.readCompanyById(companyId);

        assertTrue(actualCompany.isPresent());
        assertEquals(expectedCompany, actualCompany.get());
    }

    @Test
    public void testReadCompanyById_NotFound() {
        Long companyId = 1L;

        Mockito.when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        Optional<Company> actualCompany = companyService.readCompanyById(companyId);

        assertFalse(actualCompany.isPresent());
    }

    @Test
    public void testUpdateCompany_UpdatesAllFields() {
        Long existingCompanyId = 0L;
        Company existingCompany = new Company(existingCompanyId, "Company A", "Description A", "TX", "Austin", "456 Elm St", "987-654-3210");
        Company updatedCompany = new Company(existingCompanyId, "Updated Company Name", "Updated Description", "CA", "San Francisco", "New Address", "New Contact");

        Mockito.when(companyRepository.getReferenceById(existingCompanyId)).thenReturn(existingCompany);
        Mockito.when(companyRepository.save(updatedCompany)).thenReturn(updatedCompany);

        companyService.updateCompany(updatedCompany);

        // Verify that the existing company object is updated with all new values
        assertEquals(updatedCompany.getCompany_name(), existingCompany.getCompany_name());
        assertEquals(updatedCompany.getCompany_description(), existingCompany.getCompany_description());
        assertEquals(updatedCompany.getCompany_state(), existingCompany.getCompany_state());
        assertEquals(updatedCompany.getCompany_city(), existingCompany.getCompany_city());
        assertEquals(updatedCompany.getCompany_address(), existingCompany.getCompany_address());
        assertEquals(updatedCompany.getCompany_contact(), existingCompany.getCompany_contact());
    }

    @Test
    public void testDeleteCompany() {
        Company testCompany = new Company();
        testCompany.setCompany_Id(1L);

        Mockito.when(companyRepository.save(testCompany)).thenReturn(testCompany);
        Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(testCompany));

        Long createdCompanyId = companyService.createCompany(testCompany);
        assertEquals(testCompany.getCompany_Id(), createdCompanyId);

        Optional<Company> verifiedCompany = companyService.readCompanyById(createdCompanyId);
        assertTrue(verifiedCompany.isPresent());

        Mockito.doNothing().when(companyRepository).deleteById(1L);
        String deleted = companyService.deleteCompany(createdCompanyId);
        assertTrue(deleted.contains(createdCompanyId.toString()));

        Mockito.when(companyRepository.findById(createdCompanyId)).thenReturn(Optional.empty());
        Optional<Company> verifyDeleted = companyService.readCompanyById(createdCompanyId);
        assertFalse(verifyDeleted.isPresent());
    }
}
