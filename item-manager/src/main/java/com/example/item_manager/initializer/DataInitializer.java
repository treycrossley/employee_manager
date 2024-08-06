package com.example.item_manager.initializer;

import org.springframework.http.HttpHeaders;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataInitializer implements ApplicationRunner {

    private final RestTemplate restTemplate;
    private String bearerToken;

    public DataInitializer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeUsers();
        loginAdminUser();
        initializeCompanies();
        initializeEmployees();
    }

    void initializeUsers() {
        String registerUrl = "http://localhost:8080/api/users/register";

        // User data for registration
        String[] users = {
                "{\"username\": \"testuser\", \"password\": \"testpassword\", \"role\": \"USER\"}",
                "{\"username\": \"admin\", \"password\": \"adminpassword\", \"role\": \"ADMIN\"}"
        };

        for (String user : users) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request = new HttpEntity<>(user, headers);
                restTemplate.postForEntity(registerUrl, request, String.class);
                System.out.println("User registered: " + user);
            } catch (Exception e) {
                System.out.println("User already exists or registration failed: " + e.getMessage());
            }
        }
    }

    void loginAdminUser() {
        String loginUrl = "http://localhost:8080/api/users/login";

        // Admin user login data
        String adminLoginJson = "{\"username\": \"admin\", \"password\": \"adminpassword\"}";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(adminLoginJson, headers);
            String response = restTemplate.postForObject(loginUrl, request, String.class);

            // Extract the Bearer Token from the response
            if (response != null) {
                // Assuming the token is returned in the response as a JSON object with a
                // "token" field
                // Modify this part according to your actual response structure
                String token = response; // Replace this with actual JSON parsing if needed
                bearerToken = token;
                System.out.println("Admin Bearer token: " + bearerToken);
            }
        } catch (Exception e) {
            System.out.println("Admin login failed: " + e.getMessage());
        }
    }

    void initializeCompanies() {
        String createCompanyUrl = "http://localhost:8080/api/companies";

        // Company data for insertion
        String companyJson = "{\"name\": \"Revature\", \"location\": \"Reston, VA\"}";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (bearerToken != null)
                headers.set("Authorization", bearerToken);

            HttpEntity<String> request = new HttpEntity<>(companyJson, headers);
            restTemplate.postForEntity(createCompanyUrl, request, String.class);
            System.out.println("Company created: " + companyJson);
        } catch (Exception e) {
            System.out.println("Company creation failed: " + e.getMessage());
        }
    }

    void initializeEmployees() {
        String createEmployeeUrl = "http://localhost:8080/api/employees";

        // Employee data for insertion
        String[] employees = {
                "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"555-1234\", \"jobId\": \"IT_PROG\", \"salary\": 60000, \"companyId\": 1, \"userId\": 1}",
                "{\"firstName\": \"Jane\", \"lastName\": \"Smith\", \"email\": \"jane.smith@example.com\", \"phoneNumber\": \"555-5678\", \"jobId\": \"HR_REP\", \"salary\": 50000, \"companyId\": 1, \"userId\": 2}",
                "{\"firstName\": \"Bob\", \"lastName\": \"Johnson\", \"email\": \"bob.johnson@example.com\", \"phoneNumber\": \"555-8765\", \"jobId\": \"FIN_MGR\", \"salary\": 70000, \"companyId\": 1, \"userId\": 1}"
        };

        for (String employee : employees) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                // Add Authorization header with the Bearer token
                if (bearerToken != null)
                    headers.set("Authorization", bearerToken);

                HttpEntity<String> request = new HttpEntity<>(employee, headers);
                restTemplate.postForEntity(createEmployeeUrl, request, String.class);
                System.out.println("Employee created: " + employee);
            } catch (Exception e) {
                System.out.println("Employee creation failed: " + e.getMessage());
            }
        }
    }

}
