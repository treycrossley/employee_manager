package com.example.item_manager.controller;

import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.service.EmployeeService;
import com.example.item_manager.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock testUser and testEmployee for testing
        testUser = new User();
        testUser.setUsername("testUser");

        testAdminUser = new User(); // Initialize the test admin user
        testAdminUser.setUsername("adminUser"); // Set a username or other attributes

        testEmployee = new Employee();
        testEmployee.setUser(testUser);

    }

    @Test
    void testCreateEmployee() {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployee);

        Employee createdEmployee = employeeController.createEmployee(testEmployee);

        assertEquals(testEmployee, createdEmployee);
        verify(employeeService).createEmployee(any(Employee.class));
    }

    @Test
    void deleteEmployee_Success() {
        Long employeeId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);
        testEmployee.setUser(testUser);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));

        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId, authentication);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService).deleteEmployee(employeeId);
    }

    @Test
    void deleteEmployee_Success_Admin() {
        Long employeeId = 1L;
        String adminUsername = "adminUser";

        when(authentication.getName()).thenReturn(adminUsername);
        User adminUser = new User();
        adminUser.setUsername(adminUsername);

        when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(adminUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(userService.isAdmin(adminUser)).thenReturn(true); // Is admin

        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService).deleteEmployee(employeeId);
    }

    @Test
    void deleteEmployee_EmployeeNotFound() {
        Long employeeId = 1L;
        String username = "testUser";

        // Mock the authentication to return the defined username
        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);

        // Set up the mock behavior for user retrieval
        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty()); // No employee found

        // Call the deleteEmployee method and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.deleteEmployee(employeeId, authentication);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void deleteEmployee_UserNotFound() {
        Long employeeId = 1L;
        String username = "unknownUser";

        // Mock the authentication to return the defined username
        when(authentication.getName()).thenReturn(username);

        // Set up the mock behavior for user retrieval
        when(userService.findByUsername(username)).thenReturn(Optional.empty()); // User not found

        // Call the deleteEmployee method and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.deleteEmployee(employeeId, authentication);
        });

        assertEquals("Current user not found", exception.getMessage());
    }

    @Test
    void deleteEmployee_Forbidden() {
        Long employeeId = 1L;
        String username = "testUser";
        User otherUser = new User(); // Another user who does not own the employee

        // Mock the authentication to return the defined username
        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);
        testEmployee.setUser(otherUser); // Employee owned by another user

        // Set up the mock behavior for user and employee retrieval
        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(userService.isAdmin(testUser)).thenReturn(false); // Not an admin

        // Call the deleteEmployee method
        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId, authentication);

        // Assert the response status is 403 Forbidden
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updateEmployee_Success() {
        Long employeeId = 1L;
        String username = "testUser";
        Employee employeeDetails = new Employee();

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);
        testEmployee.setUser(testUser);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(testEmployee);

        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, employeeDetails,
                authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEmployee, response.getBody());
        verify(employeeService).updateEmployee(eq(employeeId), any(Employee.class));
    }

    @Test
    void updateEmployee_Success_Admin() {
        Long employeeId = 1L;
        String adminUsername = "adminUser";
        Employee employeeDetails = new Employee();

        when(authentication.getName()).thenReturn(adminUsername);
        testAdminUser.setUsername(adminUsername);

        when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(testEmployee);
        when(userService.isAdmin(testAdminUser)).thenReturn(true);

        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, employeeDetails,
                authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEmployee, response.getBody());
        verify(employeeService).updateEmployee(eq(employeeId), any(Employee.class));
    }

    @Test
    void updateEmployee_EmployeeNotFound() {
        Long employeeId = 1L;
        String username = "testUser";
        Employee employeeDetails = new Employee();

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.updateEmployee(employeeId, employeeDetails, authentication);
        });

        assertEquals("Employee not found", exception.getMessage());
    }

    @Test
    void updateEmployee_UserNotFound() {
        Long employeeId = 1L;
        String username = "unknownUser";
        Employee employeeDetails = new Employee();

        when(authentication.getName()).thenReturn(username);

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.updateEmployee(employeeId, employeeDetails, authentication);
        });

        assertEquals("Current user not found", exception.getMessage());
    }

    @Test
    void updateEmployee_Forbidden() {
        Long employeeId = 1L;
        String username = "testUser";
        Employee employeeDetails = new Employee();
        User otherUser = new User();

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);
        testEmployee.setUser(otherUser);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(userService.isAdmin(testUser)).thenReturn(false);

        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, employeeDetails,
                authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getEmployeeById_Success_User() {
        Long employeeId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEmployee, response.getBody());
    }

    @Test
    void getEmployeeById_Success_Admin() {
        Long employeeId = 1L;
        String adminUsername = "adminUser";

        when(authentication.getName()).thenReturn(adminUsername);
        testAdminUser.setUsername(adminUsername);

        when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(userService.isAdmin(testAdminUser)).thenReturn(true);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testEmployee, response.getBody());
    }

    @Test
    void getEmployeeById_NotFound_User() {
        Long employeeId = 1L;
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            employeeController.getEmployeeById(employeeId, authentication);
        });
    }

    @Test
    void getEmployeeById_NotFound_Admin() {
        Long employeeId = 1L;
        String adminUsername = "adminUser";

        when(authentication.getName()).thenReturn(adminUsername);
        when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(testAdminUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            employeeController.getEmployeeById(employeeId, authentication);
        });
    }

    @Test
    void getEmployeeById_Forbidden() {
        Long employeeId = 1L;
        String username = "testUser";
        User otherUser = new User();
        otherUser.setUsername("otherUser"); // Create a different user

        when(authentication.getName()).thenReturn(username);
        testUser.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(Optional.of(testUser));
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(testEmployee));
        when(userService.isAdmin(testUser)).thenReturn(false);

        Employee mockEmployee = Mockito.mock(Employee.class); // Create a mock employee
        when(mockEmployee.getUser()).thenReturn(otherUser); // Set the return value for getUser

        // Set the mock employee as the return value for employeeService.getEmployeeById
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(mockEmployee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId, authentication);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
