package com.example.item_manager.service;

import com.example.item_manager.model.Employee;
import com.example.item_manager.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee createItem(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllItems() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getItemById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee updateItem(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        employee.setName(employeeDetails.getName());
        employee.setDescription(employeeDetails.getDescription());
        return employeeRepository.save(employee);
    }

    public void deleteItem(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        employeeRepository.delete(employee);
    }
}
