package com.example.item_manager.controller;

import com.example.item_manager.model.User;
import com.example.item_manager.service.UserService;
import com.example.item_manager.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all Users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Delete a User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        if (user.getRole() == null || user.getRole().isEmpty())
            user.setRole("USER"); // Default role

        User savedUser = userService.save(user);

                String responseMessage = String.format(
                "User registered successfully: {\"id\": %d, \"username\": \"%s\", \"role\": \"%s\"}",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

        // Retrieve the stored encoded password
        String storedEncodedPassword = existingUser.get().getPassword();
        if (!passwordEncoder.matches(user.getPassword(), storedEncodedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(existingUser.get().getUsername(), existingUser.get().getRole());
        return ResponseEntity.ok("Bearer " + token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

        User user = userService.getUserById(id);

        if (user.getId().equals(currentUser.getId()) ||
                currentUser.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication
                .getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (currentUser.getId().equals(id) ||
                currentUser.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {

            userService.updateUser(id, userDetails);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<User> changePassword(@PathVariable Long id, @RequestBody String newPassword,
            Authentication authentication) {
        User currentUser = userService.findByUsername(authentication
                .getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userService.isAdminOrCurrent(id, currentUser)) {
            userService.changePassword(id, newPassword);
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username, Authentication authentication) {
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User userRequested = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if ((username.equals(currentUsername) ||
                currentUser.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN")))) {
            return ResponseEntity.ok(userRequested);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long userId, @RequestParam String role) {
        String result = userService.updateUserRole(userId, role);

        if (result.equals("User role updated successfully.")) {
            return ResponseEntity.ok(result);
        } else if (result.equals("User not found.")) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
