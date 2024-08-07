package com.example.item_manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.example.item_manager.model.Company;
import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.service.EmployeeService;
import com.example.item_manager.service.UserService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTests {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;


    private Employee testEmployee;
    private User testUser;
    private User testAdminUser;
    private Company testCompany;

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password123"); // Hash this in a real scenario
        testUser.setRole("USER");

        // Initialize the test admin user
        testAdminUser = new User();
        testAdminUser.setUsername("adminUser");
        testAdminUser.setPassword("adminPassword"); // Hash this in a real scenario
        testAdminUser.setRole("ADMIN");

        // Initialize the test company
        testCompany = new Company();
        testCompany.setCompanyId(1L);
        testCompany.setName("Test Company");
        testCompany.setLocation("Sample Location"); // Set the location
        // ... set other properties as needed

        // Initialize the test employee
        testEmployee = new Employee();
        testEmployee.setId(1L); // Set the employee ID
        testEmployee.setFirstName("John"); // Set first name
        testEmployee.setLastName("Doe"); // Set last name
        testEmployee.setEmail("john.doe@example.com"); // Set email
        testEmployee.setPhoneNumber("123-456-7890"); // Set phone number
        testEmployee.setJobId("SE123"); // Set job ID
        testEmployee.setSalary(75000.00f); // Set salary
        testEmployee.setCompany(testCompany); // Associate the test company
        testEmployee.setUser(testUser); // Associate the test user

    }

    @Test
    void testCreateEmployee() {
        // Given
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployee);
        // When
        ResponseEntity<Employee> response = employeeController.createEmployee(testEmployee);
        Employee createdEmployee = response.getBody(); // Get the Employee from the ResponseEntity
        // Then
        assertEquals(testEmployee, createdEmployee);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Optionally check the status code
        verify(employeeService).createEmployee(any(Employee.class));
    }

    // @Test
    // void deleteEmployee_Success() {
    // Long employeeId = 1L;
    // String username = "testUser";

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);
    // testEmployee.setUser(testUser);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));

    // ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId,
    // authentication);
    // assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    // verify(employeeService).deleteEmployee(employeeId);
    // }

    // @Test
    // void deleteEmployee_Success_Admin() {
    // Long employeeId = 1L;
    // String adminUsername = "adminUser";

    // when(authentication.getName()).thenReturn(adminUsername);
    // User adminUser = new User();
    // adminUser.setUsername(adminUsername);

    // when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(adminUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(userService.isAdmin(adminUser)).thenReturn(true); // Is admin

    // ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId,
    // authentication);

    // assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    // verify(employeeService).deleteEmployee(employeeId);
    // }

    // @Test
    // void deleteEmployee_EmployeeNotFound() {
    // Long employeeId = 1L;
    // String username = "testUser";

    // // Mock the authentication to return the defined username
    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);

    // // Set up the mock behavior for user retrieval
    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
    // // No employee found

    // // Call the deleteEmployee method and expect an exception
    // Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.deleteEmployee(employeeId, authentication);
    // });

    // assertEquals("Employee not found", exception.getMessage());
    // }

    // @Test
    // void deleteEmployee_UserNotFound() {
    // Long employeeId = 1L;
    // String username = "unknownUser";

    // // Mock the authentication to return the defined username
    // when(authentication.getName()).thenReturn(username);

    // // Set up the mock behavior for user retrieval
    // when(userService.findByUsername(username)).thenReturn(Optional.empty()); //
    // User not found

    // // Call the deleteEmployee method and expect an exception
    // Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.deleteEmployee(employeeId, authentication);
    // });

    // assertEquals("Current user not found", exception.getMessage());
    // }

    // @Test
    // void deleteEmployee_Forbidden() {
    // Long employeeId = 1L;
    // String username = "testUser";
    // User otherUser = new User(); // Another user who does not own the employee

    // // Mock the authentication to return the defined username
    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);
    // testEmployee.setUser(otherUser); // Employee owned by another user

    // // Set up the mock behavior for user and employee retrieval
    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(userService.isAdmin(testUser)).thenReturn(false); // Not an admin

    // // Call the deleteEmployee method
    // ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId,
    // authentication);

    // // Assert the response status is 403 Forbidden
    // assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    // }

    // @Test
    // void updateEmployee_Success() {
    // Long employeeId = 1L;
    // String username = "testUser";
    // Employee employeeDetails = new Employee();

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);
    // testEmployee.setUser(testUser);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(employeeService.updateEmployee(eq(employeeId),
    // any(Employee.class))).thenReturn(testEmployee);

    // ResponseEntity<Employee> response =
    // employeeController.updateEmployee(employeeId, employeeDetails,
    // authentication);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals(testEmployee, response.getBody());
    // verify(employeeService).updateEmployee(eq(employeeId), any(Employee.class));
    // }

    // @Test
    // void updateEmployee_Success_Admin() {
    // Long employeeId = 1L;
    // String adminUsername = "adminUser";
    // Employee employeeDetails = new Employee();

    // when(authentication.getName()).thenReturn(adminUsername);
    // testAdminUser.setUsername(adminUsername);

    // when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(employeeService.updateEmployee(eq(employeeId),
    // any(Employee.class))).thenReturn(testEmployee);
    // when(userService.isAdmin(testAdminUser)).thenReturn(true);

    // ResponseEntity<Employee> response =
    // employeeController.updateEmployee(employeeId, employeeDetails,
    // authentication);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals(testEmployee, response.getBody());
    // verify(employeeService).updateEmployee(eq(employeeId), any(Employee.class));
    // }

    // @Test
    // void updateEmployee_EmployeeNotFound() {
    // Long employeeId = 1L;
    // String username = "testUser";
    // Employee employeeDetails = new Employee();

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

    // Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.updateEmployee(employeeId, employeeDetails,
    // authentication);
    // });

    // assertEquals("Employee not found", exception.getMessage());
    // }

    // @Test
    // void updateEmployee_UserNotFound() {
    // Long employeeId = 1L;
    // String username = "unknownUser";
    // Employee employeeDetails = new Employee();

    // when(authentication.getName()).thenReturn(username);

    // when(userService.findByUsername(username)).thenReturn(Optional.empty());

    // Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.updateEmployee(employeeId, employeeDetails,
    // authentication);
    // });

    // assertEquals("Current user not found", exception.getMessage());
    // }

    // @Test
    // void updateEmployee_Forbidden() {
    // Long employeeId = 1L;
    // String username = "testUser";
    // Employee employeeDetails = new Employee();
    // User otherUser = new User();

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);
    // testEmployee.setUser(otherUser);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(userService.isAdmin(testUser)).thenReturn(false);

    // ResponseEntity<Employee> response =
    // employeeController.updateEmployee(employeeId, employeeDetails,
    // authentication);

    // assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    // }

    // @Test
    // void getEmployeeById_Success_User() {
    // Long employeeId = 1L;
    // String username = "testUser";

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));

    // ResponseEntity<Employee> response =
    // employeeController.getEmployeeById(employeeId, authentication);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals(testEmployee, response.getBody());
    // }

    // @Test
    // void getEmployeeById_Success_Admin() {
    // Long employeeId = 1L;
    // String adminUsername = "adminUser";

    // when(authentication.getName()).thenReturn(adminUsername);
    // testAdminUser.setUsername(adminUsername);

    // when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(userService.isAdmin(testAdminUser)).thenReturn(true);

    // ResponseEntity<Employee> response =
    // employeeController.getEmployeeById(employeeId, authentication);

    // assertEquals(HttpStatus.OK, response.getStatusCode());
    // assertEquals(testEmployee, response.getBody());
    // }

    // @Test
    // void getEmployeeById_NotFound_User() {
    // Long employeeId = 1L;
    // String username = "testUser";

    // when(authentication.getName()).thenReturn(username);
    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

    // assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.getEmployeeById(employeeId, authentication);
    // });
    // }

    // @Test
    // void getEmployeeById_NotFound_Admin() {
    // Long employeeId = 1L;
    // String adminUsername = "adminUser";

    // when(authentication.getName()).thenReturn(adminUsername);
    // when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

    // assertThrows(IllegalArgumentException.class, () -> {
    // employeeController.getEmployeeById(employeeId, authentication);
    // });
    // }

    // @Test
    // void getEmployeeById_Forbidden() {
    // Long employeeId = 1L;
    // String username = "testUser";
    // User otherUser = new User();
    // otherUser.setUsername("otherUser"); // Create a different user

    // when(authentication.getName()).thenReturn(username);
    // testUser.setUsername(username);

    // when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
    // when(userService.isAdmin(testUser)).thenReturn(false);

    // Employee mockEmployee = Mockito.mock(Employee.class); // Create a mock
    // employee
    // when(mockEmployee.getUser()).thenReturn(otherUser); // Set the return value
    // for getUser

    // // Set the mock employee as the return value for
    // employeeService.getEmployeeById
    // when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(mockEmployee));

    // ResponseEntity<Employee> response =
    // employeeController.getEmployeeById(employeeId, authentication);

    // assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    // }

}
