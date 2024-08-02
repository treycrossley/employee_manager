package com.example.item_manager.service;

import com.example.item_manager.model.Employee;
import com.example.item_manager.repository.EmployeeRepository;
import com.example.item_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private UserService userService;

     public Employee createEmployee(Employee employee, Long userId) {
        User user = userService.getUserById(userId); 
        employee.setUser(user); 
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employee.getPhoneNumber());
        employee.setJobId(employee.getJobId());
        employee.setSalary(employee.getSalary());
        employee.setCompany(employee.getCompany());
        if (userId != null) {
            User user = userService.getUserById(userId);
            employee.setUser(user);
        }
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        employeeRepository.delete(employee);
    }

    public List<Employee> getEmployeesByUser(Long userId) {
        User user = userService.getUserById(userId); 
        return employeeRepository.findByUser(user); 
    }
}
