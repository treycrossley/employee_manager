package com.example.item_manager.initializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

@TestPropertySource(properties = {
                "SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "SPRING_DATASOURCE_USERNAME=sa",
                "SPRING_DATASOURCE_PASSWORD=password"
})
@ExtendWith(MockitoExtension.class)
class DataInitializerTests {

    @InjectMocks
    private DataInitializer dataInitializer;

    @Mock
    private RestTemplate restTemplate;

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
