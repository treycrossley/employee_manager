package com.example.item_manager.repository;

import com.example.item_manager.model.Company;
import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private User testUser;
    private Employee testEmployee;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole("USER");
        userRepository.save(testUser);

        testCompany = new Company();
        testCompany.setName("Revature");
        testCompany.setLocation("Reston, VA");
        companyRepository.save(testCompany);

        // Create and save a test employee
        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setPhoneNumber("555-1234");
        testEmployee.setJobId("IT_PROG");
        testEmployee.setSalary(60000.0f);
        testEmployee.setUser(testUser);
        testEmployee.setCompany(testCompany);

        employeeRepository.save(testEmployee);
    }

    @Test
    void testFindByFirstName() {
        List<Employee> foundEmployees = employeeRepository.findByFirstName("John");

        assertEquals(1, foundEmployees.size(), "There should be one employee found");
        assertEquals(testEmployee.getFirstName(), foundEmployees.get(0).getFirstName(), "First names should match");
    }

    @Test
    void testFindByUser() {
        List<Employee> foundEmployees = employeeRepository.findByUser(testUser);

        assertEquals(1, foundEmployees.size(), "There should be one employee found for the user");
        assertEquals(testEmployee.getFirstName(), foundEmployees.get(0).getFirstName(), "First names should match");
    }
}
