package com.example.item_manager.controller;

import com.example.item_manager.model.Employee;
import com.example.item_manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private EmployeeService employeeService;

    // Create a new Item
    @PostMapping
    public Employee createItem(@RequestBody Employee employee) {
        return employeeService.createItem(employee);
    }

    // Get all Items
    @GetMapping
    public List<Employee> getAllItems() {
        return employeeService.getAllItems();
    }

    // Get an Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getItemById(@PathVariable Long id) {
        return employeeService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an Item
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateItem(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return ResponseEntity.ok(employeeService.updateItem(id, employeeDetails));
    }

    // Delete an Item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        employeeService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
