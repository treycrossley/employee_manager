package com.example.item_manager.service;

import com.example.item_manager.model.Company;
import com.example.item_manager.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {
    CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Company saveCompany(Company company){
        return companyRepository.save(company);
    }

    public List<Company> getCompanyByName(String companyName){
        return companyRepository.findCompaniesByName(companyName);
    }

    public List<Company> getCompaniesByLocation(String location){
        return companyRepository.findCompaniesByLocation(location);
    }

}