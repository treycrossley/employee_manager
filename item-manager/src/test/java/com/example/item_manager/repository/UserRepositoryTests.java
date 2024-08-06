package com.example.item_manager.repository;

import com.example.item_manager.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("USER");
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByUsername("nonexistentuser");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUsername("nonexistentuser");
    }
}
