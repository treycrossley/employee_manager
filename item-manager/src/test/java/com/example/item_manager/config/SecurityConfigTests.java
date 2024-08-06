package com.example.item_manager.config;

import com.example.item_manager.ItemManagerApplication;
import com.example.item_manager.service.UserService;
import com.example.item_manager.util.JwtUtil;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ItemManagerApplication.class) // Use main application class
@TestPropertySource(properties = {
        "SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "SPRING_DATASOURCE_USERNAME=sa",
        "SPRING_DATASOURCE_PASSWORD=password"
})
class SecurityConfigTests {

    @MockBean
    private UserService userService; // Mock UserService

    @MockBean
    private JwtUtil jwtUtil; // Mock JwtUtil

    @Autowired
    private WebApplicationContext context; // Inject the web application context

    @Autowired
    private SecurityConfig securityConfig; // Autowire the SecurityConfig

    @Test
    void testSecurityFilterChainBean() throws Exception {
        HttpSecurity httpSecurity = context.getBean(HttpSecurity.class); // Get the HttpSecurity bean
        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurity);
        assertNotNull(securityFilterChain, "SecurityFilterChain bean should not be null");
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        assertNotNull(passwordEncoder, "PasswordEncoder bean should not be null");
    }

    @Test
    void testAuthenticationManagerBean() throws Exception {
        HttpSecurity httpSecurity = context.getBean(HttpSecurity.class); // Get the HttpSecurity bean
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(httpSecurity);
        assertNotNull(authenticationManager, "AuthenticationManager bean should not be null");
    }
}
