package com.example.item_manager.repository;

import com.example.item_manager.model.Employee;
import com.example.item_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByFirstName(String first_name);

    List<Employee> findByUser(User user);
}
