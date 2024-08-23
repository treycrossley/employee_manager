package com.example.item_manager.service;

import com.example.item_manager.model.User;
import com.example.item_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password"); // This should be an encoded password in real scenario
        testUser.setRole("USER");
    }

    @Test
    void testSave_Success() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getUsername(), savedUser.getUsername());
        verify(userRepository).save(testUser);
    }

    @Test
    void testSave_UsernameTaken() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.save(testUser);
        });

        assertEquals("Username already taken.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        boolean exists = userService.existsByUsername(testUser.getUsername());

        assertTrue(exists);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.findByUsername(testUser.getUsername());

        assertTrue(foundUser.isPresent());
        assertEquals(testUser, foundUser.get());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUserById(testUser.getId());

        assertEquals(testUser, foundUser);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(testUser.getId());
        });

        assertEquals("User not found with id: " + testUser.getId(), exception.getMessage());
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        testUser.setUsername("updateduser");
        userService.updateUser(testUser.getId(), testUser);

        assertEquals("updateduser", testUser.getUsername());
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateUserRole_Success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        String result = userService.updateUserRole(testUser.getId(), "ADMIN");

        assertEquals("User role updated successfully.", result);
        assertEquals("ADMIN", testUser.getRole());
    }

    @Test
    void testUpdateUserRole_UserNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        String result = userService.updateUserRole(testUser.getId(), "ADMIN");

        assertEquals("User not found.", result);
    }

    @Test
    void testUpdateUserRole_InvalidRole() {
        String result = userService.updateUserRole(testUser.getId(), "INVALID_ROLE");

        assertEquals("Invalid role. Use 'ADMIN' or 'USER'.", result);
    }

    @Test
    void testChangePassword() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.changePassword(testUser.getId(), "newPassword");

        assertNotEquals("password", testUser.getPassword()); // Ensure password is changed
        verify(userRepository).save(testUser);
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userService.loadUserByUsername(testUser.getUsername());

        assertEquals(testUser.getUsername(), userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(testUser.getUsername());
        });

        assertEquals("User not found with username: " + testUser.getUsername(), exception.getMessage());
    }
}
