package com.example.item_manager.repository;

import com.example.item_manager.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findCompaniesByName(String name);
    List<Company> findCompaniesByLocation(@Param("location") String location);
}
