package com.example.item_manager.repository;

import com.example.item_manager.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findCompanyByName(String name);
    List<Company> findCompanyByLocation(@Param("location") String location);
}
