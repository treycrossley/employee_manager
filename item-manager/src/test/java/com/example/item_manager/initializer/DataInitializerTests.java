package com.example.item_manager.initializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {
                    "{\"username\": \"admin\", \"password\": \"adminpassword\"}",
                    "{\"username\": \"testuser\", \"password\": \"testpassword\"}"
    })
    void testLoginUser(String loginJson) {
        // Mock the response from the login
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn("{\"token\": \"dummyToken\"}"); // Mock a JSON response with token

        // Call the method to test
        dataInitializer.loginUser(loginJson);

        // Verify that the RestTemplate was called for login
        verify(restTemplate, times(1)).postForObject(any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
                    "{\"name\": \"Tech Solutions\", \"location\": \"San Francisco, CA\"}",
                    "{\"name\": \"Innovatech\", \"location\": \"Austin, TX\"}",
                    "{\"name\": \"Global Enterprises\", \"location\": \"New York, NY\"}",
                    "{\"name\": \"Digital Dynamics\", \"location\": \"Seattle, WA\"}"
    })
    void testInitializeCompanies(String company) {
            // Mock the response from the RestTemplate
            when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                            .thenReturn(ResponseEntity.ok("Company created"));

            // Call the method to test for a single company
            dataInitializer.initializeCompanies(new String[] { company });

            // Verify that the RestTemplate was called for company creation
            verify(restTemplate, times(1)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
                    "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phoneNumber\": \"555-1234\", \"jobId\": \"IT_PROG\", \"salary\": 60000, \"companyId\": 1, \"userId\": 1}",
                    "{\"firstName\": \"Jane\", \"lastName\": \"Smith\", \"email\": \"jane.smith@example.com\", \"phoneNumber\": \"555-5678\", \"jobId\": \"HR_REP\", \"salary\": 50000, \"companyId\": 1, \"userId\": 2}",
                    "{\"firstName\": \"Bob\", \"lastName\": \"Johnson\", \"email\": \"bob.johnson@example.com\", \"phoneNumber\": \"555-8765\", \"jobId\": \"FIN_MGR\", \"salary\": 70000, \"companyId\": 1, \"userId\": 0}",
                    "{\"firstName\": \"Alice\", \"lastName\": \"Williams\", \"email\": \"alice.williams@example.com\", \"phoneNumber\": \"555-1111\", \"jobId\": \"DEV_SENIOR\", \"salary\": 85000, \"companyId\": 1, \"userId\": 3}",
                    "{\"firstName\": \"David\", \"lastName\": \"Brown\", \"email\": \"david.brown@example.com\", \"phoneNumber\": \"555-2222\", \"jobId\": \"QA_ENGINEER\", \"salary\": 70000, \"companyId\": 1, \"userId\": 4}",
                    "{\"firstName\": \"Emma\", \"lastName\": \"Davis\", \"email\": \"emma.davis@example.com\", \"phoneNumber\": \"555-3333\", \"jobId\": \"PROD_MGR\", \"salary\": 90000, \"companyId\": 2, \"userId\": 5}"
    })
    void testInitializeEmployees(String employee) {
            // Mock the response from the RestTemplate
            when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
                            .thenReturn(ResponseEntity.ok("Employee created"));

            // Call the method to test for a single employee
            dataInitializer.initializeEmployees(new String[] { employee });

            // Verify that the RestTemplate was called for employee creation
            verify(restTemplate, times(1)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
    }
}
