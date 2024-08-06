package com.example.item_manager.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = AppConfig.class) // Specify the configuration class to test
class AppConfigTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testRestTemplateBean() {
        // Retrieve the RestTemplate bean from the application context
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);

        // Assert that the RestTemplate bean is not null
        assertNotNull(restTemplate, "RestTemplate bean should not be null");
    }
}
