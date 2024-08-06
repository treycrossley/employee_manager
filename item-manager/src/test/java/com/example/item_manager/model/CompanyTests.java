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

    @Test
    void testAddEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("555-1234");
        employee.setJobId("IT_PROG");
        employee.setSalary(60000.0f);
        employee.setCompany(company); // Set the company

        company.getEmployees().add(employee); // Add employee to the company
        assertEquals(1, company.getEmployees().size());
        assertTrue(company.getEmployees().contains(employee));
    }

    @Test
    void testRemoveEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("555-1234");
        employee.setJobId("IT_PROG");
        employee.setSalary(60000.0f);
        employee.setCompany(company);

        company.getEmployees().add(employee); // Add employee
        company.getEmployees().remove(employee); // Remove employee
        assertEquals(0, company.getEmployees().size());
    }

    @Test
    void testEmployeeBidirectionalRelation() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jane");
        employee.setLastName("Smith");
        employee.setEmail("jane.smith@example.com");
        employee.setPhoneNumber("555-5678");
        employee.setJobId("HR_MAN");
        employee.setSalary(65000.0f);
        employee.setCompany(company); // Set the company

        company.getEmployees().add(employee);
        assertEquals(company, employee.getCompany());
    }
}
