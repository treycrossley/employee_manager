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

    // @param companyService a companyService bean that will be autowired in from the Spring IOC container.
    @Autowired
    public CompanyController(CompanyService companyService){
        this.CompanyService = companyService;
    }

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
    public ResponseEntity<Company> getCompanyById(@PathVariable int id) {
        return CompanyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get a company by Name
    @GetMapping(value = "company", params = {"companyName"})
    public List<Company> getCompanyByName(@RequestParam("companyName") String companyName){
        return CompanyService.getCompanyByName(companyName);
    }

    // Get a company by Location
    @GetMapping(value = "company", params = {"location"})
    public List<Company> getCompanyByLocation(@RequestParam("location") String location){
        return CompanyService.getCompanyByLocation(location);
    }

    // Update a company
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable int id, @RequestBody Company CompanyDetails) {
        return ResponseEntity.ok(CompanyService.updateCompany(id, CompanyDetails));
    }

    // Delete a company
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable int id) {
        CompanyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
