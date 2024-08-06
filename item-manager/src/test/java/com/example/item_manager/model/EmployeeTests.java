package com.example.item_manager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTests {

    private Employee employee;
    private Company company;
    private User user;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("Test Company");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPhoneNumber("555-1234");
        employee.setJobId("IT_PROG");
        employee.setSalary(60000.0f);
        employee.setCompany(company);
        employee.setUser(user);
    }

    @Test
    void testDefaultConstructor() {
        Employee defaultEmployee = new Employee();
        assertNull(defaultEmployee.getFirstName());
        assertNull(defaultEmployee.getLastName());
        assertNull(defaultEmployee.getEmail());
        assertNull(defaultEmployee.getPhoneNumber());
        assertNull(defaultEmployee.getJobId());
        assertEquals(0.0f, defaultEmployee.getSalary());
        assertNull(defaultEmployee.getCompany());
        assertNull(defaultEmployee.getUser());
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(employee, null); // Employee should not be equal to null
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(employee, employee); // Employee should be equal to itself
    }

    @Test
    void testNegativeSalary() {
        employee.setSalary(-5000.0f);
        assertEquals(-5000.0f, employee.getSalary());
    }

    @Test
    void testZeroSalary() {
        employee.setSalary(0.0f);
        assertEquals(0.0f, employee.getSalary());
    }

    @Test
    void testToStringWithoutCompany() {
        employee.setCompany(null);
        String expected = "Employee{id=1, firstName='John', lastName='Doe', email='john.doe@example.com', phoneNumber='555-1234', jobId='IT_PROG', salary=60000.0, company=null}";
        assertEquals(expected, employee.toString());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john.doe@example.com", employee.getEmail());
        assertEquals("555-1234", employee.getPhoneNumber());
        assertEquals("IT_PROG", employee.getJobId());
        assertEquals(60000.0f, employee.getSalary());
        assertEquals(company, employee.getCompany());
        assertEquals(user, employee.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        Employee anotherEmployee = new Employee();
        anotherEmployee.setId(1L);
        anotherEmployee.setFirstName("John");
        anotherEmployee.setLastName("Doe");
        anotherEmployee.setEmail("john.doe@example.com");
        anotherEmployee.setPhoneNumber("555-1234");
        anotherEmployee.setJobId("IT_PROG");
        anotherEmployee.setSalary(60000.0f);
        anotherEmployee.setCompany(company);
        anotherEmployee.setUser(user);

        assertEquals(employee, anotherEmployee);
        assertEquals(employee.hashCode(), anotherEmployee.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Employee{id=1, firstName='John', lastName='Doe', email='john.doe@example.com', phoneNumber='555-1234', jobId='IT_PROG', salary=60000.0, company="
                + company + "}";
        assertEquals(expected, employee.toString());
    }

    @Test
    void testTransientCompanyId() {
        employee.setCompanyId(2L);
        assertEquals(2L, employee.getCompanyId());
    }
}
