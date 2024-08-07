package com.example.item_manager.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.item_manager.model.Company;
import com.example.item_manager.service.CompanyService;

class CompanyControllerTests {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    private Company testCompany;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testCompany = new Company();
        testCompany.setCompanyId(1L);
        testCompany.setName("Revature");
        testCompany.setLocation("Reston, VA");
    }

    @Test
    void testCreateCompany() {
        when(companyService.createCompany(any(Company.class))).thenReturn(testCompany);

        Company createdCompany = companyController.createCompany(testCompany);

        assertEquals(testCompany, createdCompany);
        verify(companyService).createCompany(any(Company.class));
    }

    @Test
    void testGetAllCompanies() {
        when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(testCompany));

        List<Company> companies = companyController.getAllCompanies();

        assertEquals(1, companies.size());
        assertEquals(testCompany, companies.get(0));
        verify(companyService).getAllCompanies();
    }

    @Test
    void testGetCompanyById() {
        when(companyService.getCompanyById(1L)).thenReturn(Optional.of(testCompany));

        ResponseEntity<Company> response = companyController.getCompanyById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testCompany, response.getBody());
        verify(companyService).getCompanyById(1L);
    }

    @Test
    void testGetCompanyByIdNotFound() {
        when(companyService.getCompanyById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Company> response = companyController.getCompanyById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(companyService).getCompanyById(1L);
    }

    @Test
    void testUpdateCompany() {
        when(companyService.updateCompany(anyLong(), any(Company.class))).thenReturn(testCompany);

        ResponseEntity<Company> response = companyController.updateCompany(1L, testCompany);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testCompany, response.getBody());
        verify(companyService).updateCompany(anyLong(), any(Company.class));
    }

    @Test
    void testDeleteCompany() {
        doNothing().when(companyService).deleteCompany(1L);

        ResponseEntity<Void> response = companyController.deleteCompany(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(companyService).deleteCompany(1L);
    }
}
