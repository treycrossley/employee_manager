package com.example.item_manager.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class DataInitializer implements ApplicationRunner {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeUsers();
        initializeCompanies();
        initializeEmployees();
    }

    private void initializeUsers() {
        String registerUrl = "http://localhost:8080/api/users/register";

        // User data for registration
        String[] users = {
                "{\"username\": \"testuser\", \"password\": \"testpassword\"}",
                "{\"username\": \"admin\", \"password\": \"adminpassword\"}"
        };

        for (String user : users) {
            try {
                restTemplate.postForEntity(registerUrl, user, String.class);
                System.out.println("User registered: " + user);
            } catch (Exception e) {
                System.out.println("User already exists or registration failed: " + e.getMessage());
            }
        }
    }

    private void initializeCompanies() {
        String createCompanyUrl = "http://localhost:8080/api/companies";

        // Company data for insertion
        String companyJson = "{\"name\": \"Revature\", \"location\": \"Reston, VA\"}";

        try {
            restTemplate.postForEntity(createCompanyUrl, companyJson, String.class);
            System.out.println("Company created: " + companyJson);
        } catch (Exception e) {
            System.out.println("Company creation failed: " + e.getMessage());
        }
    }

    private void initializeEmployees() {
        String createEmployeeUrl = "http://localhost:8080/api/employees";

        // Employee data for insertion
        String[] employees = {
                "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"555-1234\", \"jobId\": \"IT_PROG\", \"salary\": 60000, \"companyId\": 1, \"userId\": 1}",
                "{\"firstName\": \"Jane\", \"lastName\": \"Smith\", \"email\": \"jane.smith@example.com\", \"phoneNumber\": \"555-5678\", \"jobId\": \"HR_REP\", \"salary\": 50000, \"companyId\": 1, \"userId\": 2}",
                "{\"firstName\": \"Bob\", \"lastName\": \"Johnson\", \"email\": \"bob.johnson@example.com\", \"phoneNumber\": \"555-8765\", \"jobId\": \"FIN_MGR\", \"salary\": 70000, \"companyId\": 1, \"userId\": 1}"
        };

        for (String employee : employees) {
            try {
                restTemplate.postForEntity(createEmployeeUrl, employee, String.class);
                System.out.println("Employee created: " + employee);
            } catch (Exception e) {
                System.out.println("Employee creation failed: " + e.getMessage());
            }
        }
    }

}
