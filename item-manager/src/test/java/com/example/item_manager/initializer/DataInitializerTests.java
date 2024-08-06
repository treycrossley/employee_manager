package com.example.item_manager.initializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataInitializerTests {

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationArguments applicationArguments; // If you need to mock arguments

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeUsers() {
        // Mock the response from the RestTemplate
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("User registered"));

        // Call the method to test
        dataInitializer.initializeUsers();

        // Verify that the RestTemplate was called for user registration
        verify(restTemplate, times(2)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testLoginAdminUser() {
        // Mock the response from the login
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn("{\"token\": \"dummyToken\"}"); // Mock a JSON response with token

        // Call the method to test
        dataInitializer.loginAdminUser();

        // Verify that the RestTemplate was called for admin login
        verify(restTemplate, times(1)).postForObject(any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testInitializeCompanies() {
        // Mock the response from the RestTemplate
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Company created"));

        // Call the method to test
        dataInitializer.initializeCompanies();

        // Verify that the RestTemplate was called for company creation
        verify(restTemplate, times(1)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testInitializeEmployees() {
        // Mock the response from the RestTemplate
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Employee created"));

        // Call the method to test
        dataInitializer.initializeEmployees();

        // Verify that the RestTemplate was called for employee creation
        verify(restTemplate, times(3)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)); // 3
                                                                                                                  // employees
    }
}
