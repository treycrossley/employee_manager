package com.example.item_manager.controller;

import com.example.item_manager.model.Company;
import com.example.item_manager.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService CompanyService;

    // Create a new company
    @PostMapping
    public Company createCompany(@RequestBody Company Company) {
        return CompanyService.createCompany(Company);
    }

    // Get all companies
    @GetMapping
    public List<Company> getAllCompanies() {
        return CompanyService.getAllCompanies();
    }

    // Get a company by ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return CompanyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a company
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company CompanyDetails) {
        return ResponseEntity.ok(CompanyService.updateCompany(id, CompanyDetails));
    }

    // Delete a company
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        CompanyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
