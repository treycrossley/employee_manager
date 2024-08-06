package com.example.item_manager.service;

import com.example.item_manager.model.Company;
import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import com.example.item_manager.repository.CompanyRepository;
import com.example.item_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    public Employee createEmployee(Employee employee) {
        User user = userService.getCurrentUser();
        employee.setUser(user);
        Long companyId = employee.getCompanyId();
        if (companyId != null) {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            employee.setCompany(company);
        } else {
            throw new RuntimeException("Company ID must be provided.");
        }
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
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            employee.setUser(currentUser);
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
