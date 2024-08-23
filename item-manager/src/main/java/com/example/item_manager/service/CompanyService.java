package com.example.item_manager.service;

import com.example.item_manager.model.Company;
import com.example.item_manager.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository CompanyRepository;

    public CompanyService(CompanyRepository companyRepository){
        this.CompanyRepository = companyRepository;
    }
    
    public Company createCompany(Company Company) {
        return CompanyRepository.save(Company);
    }

    public List<Company> getAllCompanies() {
        return CompanyRepository.findAll();
    }

    public Optional<Company> getCompanyById(int id) {
        return CompanyRepository.findById(id);
    }
    
    public List<Company> getCompanyByName(String companyName){
        return CompanyRepository.findCompanyByName(companyName);
    }

    public List<Company> getCompanyByLocation(String location){
        return CompanyRepository.findCompanyByLocation(location);
    }

    public Company updateCompany(int id, Company CompanyDetails) {
        Company Company = CompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Company.setName(CompanyDetails.getName());
        Company.setLocation(CompanyDetails.getLocation());
        return CompanyRepository.save(Company);
    }

    public void deleteCompany(int id) {
        Company Company = CompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        CompanyRepository.delete(Company);
    }
}