package com.example.item_manager.controller;

import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.service.EmployeeService;
import com.example.item_manager.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    // Create a new Employee
    @PostMapping()
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
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
