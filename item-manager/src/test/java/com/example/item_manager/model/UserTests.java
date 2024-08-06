package com.example.item_manager.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole("ADMIN");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("ADMIN", user.getRole());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void testGetAuthorities() {
        assertEquals(1, user.getAuthorities().size());
        assertEquals("ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        User anotherUser = new User();
        anotherUser.setId(1L);
        anotherUser.setUsername("testuser");
        anotherUser.setPassword("password");
        anotherUser.setRole("ADMIN");
        anotherUser.setCreatedAt(user.getCreatedAt()); // Set createdAt
        anotherUser.setUpdatedAt(user.getUpdatedAt());

        assertEquals(user, anotherUser);
        assertEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void testToString() {
        String actualString = user.toString();
        String expected = "User{id=1, username='testuser', password='password', role='ADMIN', createdAt="
                + user.getCreatedAt() + ", updatedAt=" + user.getUpdatedAt() + ", employees=[]}";
        assertEquals(expected, actualString);
    }

    @Test
    void testDefaultRole() {
        User newUser = new User();
        assertEquals("USER", newUser.getRole()); // Verify that the default role is "USER"
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(user, null); // User should not be equal to null
    }

    @Test
    void testEqualsWithSameInstance() {
        assertEquals(user, user); // User should be equal to itself
    }

    @Test
    void testEmployeesCollection() {
        assertTrue(user.getEmployees().isEmpty()); // Ensure employees collection is initially empty

        // Assuming you have an Employee class and its methods to add and remove
        // employees
        Employee employee = new Employee();
        employee.setUser(user); // Set user for the employee
        user.getEmployees().add(employee); // Add an employee

        assertFalse(user.getEmployees().isEmpty()); // Collection should no longer be empty
        assertEquals(1, user.getEmployees().size()); // Verify the size of the collection
    }

}
