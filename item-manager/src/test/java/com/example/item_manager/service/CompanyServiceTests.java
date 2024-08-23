package com.example.item_manager.service;

import com.example.item_manager.model.Company;
import com.example.item_manager.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    private Company testCompany;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test company
        testCompany = new Company();
        testCompany.setName("Revature");
        testCompany.setLocation("Reston, VA");
    }

    @Test
    void testCreateCompany() {
        when(companyRepository.save(any(Company.class))).thenReturn(testCompany);

        Company createdCompany = companyService.createCompany(testCompany);

        assertNotNull(createdCompany);
        assertEquals(testCompany.getName(), createdCompany.getName());
        assertEquals(testCompany.getLocation(), createdCompany.getLocation());
        verify(companyRepository).save(testCompany);
    }

    @Test
    void testGetAllCompanies() {
        when(companyRepository.findAll()).thenReturn(Arrays.asList(testCompany));

        List<Company> companies = companyService.getAllCompanies();

        assertNotNull(companies);
        assertEquals(1, companies.size());
        assertEquals(testCompany.getName(), companies.get(0).getName());
        verify(companyRepository).findAll();
    }

    @Test
    void testGetCompanyById() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(testCompany));

        Optional<Company> foundCompany = companyService.getCompanyById(1L);

        assertTrue(foundCompany.isPresent());
        assertEquals(testCompany.getName(), foundCompany.get().getName());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Company> foundCompany = companyService.getCompanyById(1L);

        assertFalse(foundCompany.isPresent());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testUpdateCompany() {
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Company");
        updatedCompany.setLocation("Updated Location");

        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(testCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(testCompany);

        Company result = companyService.updateCompany(1L, updatedCompany);

        assertNotNull(result);
        assertEquals(updatedCompany.getName(), result.getName());
        assertEquals(updatedCompany.getLocation(), result.getLocation());
        verify(companyRepository).findById(1L);
        verify(companyRepository).save(testCompany);
    }

    @Test
    void testUpdateCompany_NotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            companyService.updateCompany(1L, testCompany);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testDeleteCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(testCompany));

        companyService.deleteCompany(1L);

        verify(companyRepository).delete(testCompany);
    }

    @Test
    void testDeleteCompany_NotFound() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            companyService.deleteCompany(1L);
        });

        assertEquals("Item not found", exception.getMessage());
        verify(companyRepository).findById(1L);
    }
}
