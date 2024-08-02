package com.example.item_manager.repository;

import com.example.item_manager.model.Company;
import com.example.item_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByName(String name);

}
