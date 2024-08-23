package com.example.item_manager.repository;

import com.example.item_manager.model.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyRepositoryTests {

    @Mock
    private CompanyRepository companyRepository;

    private Company company;

    @BeforeEach
    public void setUp() {
        company = new Company();
        company.setCompanyId(1L);
        company.setName("Revature");
        company.setLocation("Reston, VA");
    }

    @Test
    void testFindCompanyByName() {
        List<Company> companies = new ArrayList<>();
        companies.add(company);

        when(companyRepository.findCompanyByName("Revature")).thenReturn(companies);

        List<Company> result = companyRepository.findCompanyByName("Revature");

        assertEquals(1, result.size());
        assertEquals("Revature", result.get(0).getName());
        verify(companyRepository, times(1)).findCompanyByName("Revature");
    }

    @Test
    void testFindCompanyByLocation() {
        List<Company> companies = new ArrayList<>();
        companies.add(company);

        when(companyRepository.findCompanyByLocation("Reston, VA")).thenReturn(companies);

        List<Company> result = companyRepository.findCompanyByLocation("Reston, VA");

        assertEquals(1, result.size());
        assertEquals("Reston, VA", result.get(0).getLocation());
        verify(companyRepository, times(1)).findCompanyByLocation("Reston, VA");
    }
}
