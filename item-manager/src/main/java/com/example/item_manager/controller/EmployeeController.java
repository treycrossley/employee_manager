package com.example.item_manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.service.EmployeeService;
import com.example.item_manager.service.UserService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private UserService userService;

    public EmployeeController(EmployeeService employeeService, UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
    }

    // Create a new Employee
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        ResponseEntity<Employee> response = ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        return response;
    }

    // Get all Employees
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        if (userService.isAdmin(currentUser)) {
            List<Employee> allEmployees = employeeService.getAllEmployees();
            return ResponseEntity.ok(allEmployees);
        }
        List<Employee> userEmployees = employeeService.getEmployeesByUser(currentUser.getId());
        return ResponseEntity.ok(userEmployees);
    }

    // Get an Employee by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Employee employee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (employee.getUser().equals(currentUser) || userService.isAdmin(currentUser)) {
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Update an Employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Employee existingEmployee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (existingEmployee.getUser().equals(currentUser) || userService.isAdmin(currentUser)) {
            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
            return ResponseEntity.ok(updatedEmployee);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Delete an Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        Employee existingEmployee = employeeService.getEmployeeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (existingEmployee.getUser().equals(currentUser) || userService.isAdmin(currentUser)) {
            employeeService.deleteEmployee(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
