package com.example.item_manager.service;

import com.example.item_manager.model.Employee;
import com.example.item_manager.model.Company;
import com.example.item_manager.repository.CompanyRepository;
import com.example.item_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    private CompanyRepository companyRepository;

    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository){
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    public Employee createEmployee(int companyID, Employee employee) {
        Company c = companyRepository.findById(companyID).get();
        c.getEmployees().add(employee);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(int id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(int id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setJobId(employeeDetails.getJobId());
        employee.setSalary(employeeDetails.getSalary());
        employee.setCompany(employeeDetails.getCompany());
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        employeeRepository.delete(employee);
    }
}
