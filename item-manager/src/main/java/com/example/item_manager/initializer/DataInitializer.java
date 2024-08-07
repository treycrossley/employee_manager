package com.example.item_manager.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {

    private final RestTemplate restTemplate;
    private String bearerToken;

    protected String[] users = {
            "{\"username\": \"testuser\", \"password\": \"testpassword\", \"role\": \"USER\"}",
            "{\"username\": \"admin\", \"password\": \"adminpassword\", \"role\": \"ADMIN\"}"
    };

    // Employee data for insertion
    String[] employees = {
            "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"555-1234\", \"jobId\": \"IT_PROG\", \"salary\": 60000, \"companyId\": 1, \"userId\": 1}",
            "{\"firstName\": \"Jane\", \"lastName\": \"Smith\", \"email\": \"jane.smith@example.com\", \"phoneNumber\": \"555-5678\", \"jobId\": \"HR_REP\", \"salary\": 50000, \"companyId\": 1, \"userId\": 2}",
            "{\"firstName\": \"Bob\", \"lastName\": \"Johnson\", \"email\": \"bob.johnson@example.com\", \"phoneNumber\": \"555-8765\", \"jobId\": \"FIN_MGR\", \"salary\": 70000, \"companyId\": 1, \"userId\": 0}",
            "{\"firstName\": \"Alice\", \"lastName\": \"Williams\", \"email\": \"alice.williams@example.com\", \"phoneNumber\": \"555-1111\", \"jobId\": \"DEV_SENIOR\", \"salary\": 85000, \"companyId\": 1, \"userId\": 3}",
            "{\"firstName\": \"David\", \"lastName\": \"Brown\", \"email\": \"david.brown@example.com\", \"phoneNumber\": \"555-2222\", \"jobId\": \"QA_ENGINEER\", \"salary\": 70000, \"companyId\": 1, \"userId\": 4}",
            "{\"firstName\": \"Emma\", \"lastName\": \"Davis\", \"email\": \"emma.davis@example.com\", \"phoneNumber\": \"555-3333\", \"jobId\": \"PROD_MGR\", \"salary\": 90000, \"companyId\": 2, \"userId\": 5}"
    };

    String[] companies = {
            "{\"name\": \"Tech Solutions\", \"location\": \"San Francisco, CA\"}",
            "{\"name\": \"Innovatech\", \"location\": \"Austin, TX\"}",
            "{\"name\": \"Global Enterprises\", \"location\": \"New York, NY\"}",
            "{\"name\": \"Digital Dynamics\", \"location\": \"Seattle, WA\"}",
    };

    public DataInitializer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeUsers();
        loginUser(users[0]); // regular user
        initializeCompanies(Arrays.copyOfRange(companies, 0, 2));
        initializeEmployees(Arrays.copyOfRange(employees, 0, 3));
        loginUser(users[1]); // admin
        initializeCompanies(Arrays.copyOfRange(companies, 2, 4));
        initializeEmployees(Arrays.copyOfRange(employees, 3, 6));
    }

    void initializeUsers() {
        String registerUrl = "http://localhost:8080/api/users/register";

        for (String user : users) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> request = new HttpEntity<>(user, headers);
                restTemplate.postForEntity(registerUrl, request, String.class);
                System.out.println("User registered: " + user);
            } catch (RestClientException e) {
                System.out.println("User already exists or registration failed: " + e.getMessage());
            }
        }
    }

    void loginUser(String loginJson) {
        String loginUrl = "http://localhost:8080/api/users/login";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(loginJson, headers);
            String response = restTemplate.postForObject(loginUrl, request, String.class);

            // Extract the Bearer Token from the response
            if (response != null) {
                // Assuming the token is returned in the response as a JSON object with a
                // "token" field
                // Modify this part according to your actual response structure
                String token = response; // Replace this with actual JSON parsing if needed
                bearerToken = token;
                System.out.println("Bearer token: " + bearerToken);
            }
        } catch (RestClientException e) {
            System.out.println("login failed: " + e.getMessage());
        }
    }

    void initializeCompanies(String[] companies) {
        String createCompanyUrl = "http://localhost:8080/api/companies";

        for (String company : companies) {
            System.out.println("Creating company with payload: " + company);
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                if (bearerToken != null)
                    headers.set("Authorization", bearerToken);

                HttpEntity<String> request = new HttpEntity<>(company, headers);
                restTemplate.postForEntity(createCompanyUrl, request, String.class);
                System.out.println(company + "Company created: ");
            } catch (RestClientException e) {
                System.out.println("Company creation failed: " + e.getMessage());

            }
        }
    }

    void initializeEmployees(String[] employees) {
        String createEmployeeUrl = "http://localhost:8080/api/employees";



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
            } catch (RestClientException e) {
                System.out.println("Employee creation failed: " + e.getMessage());
            }
        }
    }

}
