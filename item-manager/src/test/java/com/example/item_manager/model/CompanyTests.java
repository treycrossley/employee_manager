package com.example.item_manager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTests {

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setCompanyId(1L);
        company.setName("Revature");
        company.setLocation("Reston, VA");
        company.setEmployees(new HashSet<>());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, company.getCompanyId());
        assertEquals("Revature", company.getName());
        assertEquals("Reston, VA", company.getLocation());
        assertNotNull(company.getEmployees());
    }

    @Test
    void testEqualsAndHashCode() {
        Company anotherCompany = new Company();
        anotherCompany.setCompanyId(1L);
        anotherCompany.setName("Revature");
        anotherCompany.setLocation("Reston, VA");
        anotherCompany.setEmployees(new HashSet<>());

        assertEquals(company, anotherCompany);
        assertEquals(company.hashCode(), anotherCompany.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Company{companyId=1, name='Revature', location='Reston, VA', employees=[]}";
        assertEquals(expected, company.toString());
    }
}
