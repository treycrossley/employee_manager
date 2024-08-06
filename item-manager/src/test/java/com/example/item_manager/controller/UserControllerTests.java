package com.example.item_manager.controller;

import com.example.item_manager.model.User;
import com.example.item_manager.service.UserService;
import com.example.item_manager.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password"); // This should be an encoded password in real scenario
        testUser.setRole("USER");
    }

    @Test
    void testRegisterUser_Success() {
        when(userService.existsByUsername(testUser.getUsername())).thenReturn(false);
        when(userService.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String responseBody = response.getBody();
        assertNotNull(responseBody, "Response body should not be null");
        assertTrue(responseBody.contains("User registered successfully"));
        verify(userService).save(testUser);
    }

    @Test
    void testRegisterUser_Conflict() {
        when(userService.existsByUsername(testUser.getUsername())).thenReturn(true);

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
        verify(userService, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        when(userService.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(testUser.getUsername(), testUser.getRole())).thenReturn("mockedToken");

        ResponseEntity<String> response = userController.loginUser(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bearer mockedToken", response.getBody());
    }

    @Test
    void testLoginUser_InvalidUsername() {
        when(userService.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        ResponseEntity<String> response = userController.loginUser(testUser);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);

        ResponseEntity<User> response = userController.getUserById(testUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testUpdateUser() {
        doNothing().when(userService).updateUser(testUser.getId(), testUser);

        ResponseEntity<User> response = userController.updateUser(testUser.getId(), testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUser(testUser.getId(), testUser);
    }

    @Test
    void testChangePasswordAsAdmin() {
        // Arrange
        when(authentication.getName()).thenReturn("currentUser");
        when(userService.findByUsername("currentUser")).thenReturn(Optional.of(testUser));
        when(userService.isAdminOrCurrent(2L, testUser)).thenReturn(true); // Simulate admin access

        // Act
        ResponseEntity<User> response = userController.changePassword(2L, "newPassword", authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).changePassword(2L, "newPassword");
    }

    @Test
    void testChangePasswordAsUser() {
        // Arrange
        when(authentication.getName()).thenReturn("currentUser");
        when(userService.findByUsername("currentUser")).thenReturn(Optional.of(testUser));
        when(userService.isAdminOrCurrent(1L, testUser)).thenReturn(true); // Simulate changing own password

        // Act
        ResponseEntity<User> response = userController.changePassword(1L, "newPassword", authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).changePassword(1L, "newPassword");
    }

    @Test
    void testChangePasswordForbidden() {
        // Arrange
        when(authentication.getName()).thenReturn("currentUser");
        when(userService.findByUsername("currentUser")).thenReturn(Optional.of(testUser));
        when(userService.isAdminOrCurrent(2L, testUser)).thenReturn(false); // Simulate forbidden access

        // Act
        ResponseEntity<User> response = userController.changePassword(2L, "newPassword", authentication);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(userService, never()).changePassword(any(Long.class), any(String.class)); // Ensure password change is
                                                                                         // not called
    }

    @Test
    void testChangePasswordUserNotFound() {
        // Arrange
        when(authentication.getName()).thenReturn("unknownUser");
        when(userService.findByUsername("unknownUser")).thenReturn(Optional.empty()); // Simulate user not found

        // Act & Assert
        try {
            userController.changePassword(1L, "newPassword", authentication);
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    @Test
    void testGetUserByUsername() {
        when(userService.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.getUserByUsername(testUser.getUsername());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userService.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByUsername(testUser.getUsername());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUserRole() {
        when(userService.updateUserRole(testUser.getId(), "ADMIN")).thenReturn("User role updated successfully.");

        ResponseEntity<String> response = userController.updateUserRole(testUser.getId(), "ADMIN");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User role updated successfully.", response.getBody());
    }

    @Test
    void testUpdateUserRole_UserNotFound() {
        when(userService.updateUserRole(testUser.getId(), "ADMIN")).thenReturn("User not found.");

        ResponseEntity<String> response = userController.updateUserRole(testUser.getId(), "ADMIN");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUserRole_BadRequest() {
        when(userService.updateUserRole(testUser.getId(), "ADMIN")).thenReturn("Invalid role.");

        ResponseEntity<String> response = userController.updateUserRole(testUser.getId(), "ADMIN");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid role.", response.getBody());
    }
}
