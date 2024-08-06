package com.example.item_manager.controller;

import com.example.item_manager.model.Employee;
import com.example.item_manager.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTests {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setPhoneNumber("555-1234");
        testEmployee.setJobId("IT_PROG");
        testEmployee.setSalary(60000.0f);
    }

    @Test
    void testCreateEmployee() {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployee);

        Employee createdEmployee = employeeController.createEmployee(testEmployee);

        assertEquals(testEmployee, createdEmployee);
        verify(employeeService).createEmployee(any(Employee.class));
    }

    @Test
    void testGetAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(testEmployee));

        List<Employee> employees = employeeController.getAllEmployees();

        assertEquals(1, employees.size());
        assertEquals(testEmployee, employees.get(0));
        verify(employeeService).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(testEmployee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testEmployee, response.getBody());
        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Employee> response = employeeController.getEmployeeById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(testEmployee);

        ResponseEntity<Employee> response = employeeController.updateEmployee(1L, testEmployee);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(testEmployee, response.getBody());
        verify(employeeService).updateEmployee(anyLong(), any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(1L);

        ResponseEntity<Void> response = employeeController.deleteEmployee(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(employeeService).deleteEmployee(1L);
    }
}
