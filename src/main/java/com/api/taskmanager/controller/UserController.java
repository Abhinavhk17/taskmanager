package com.api.taskmanager.controller;

import com.api.taskmanager.dto.MessageResponse;
import com.api.taskmanager.dto.UserProfileUpdateRequest;
import com.api.taskmanager.model.User;
import com.api.taskmanager.security.UserDetailsImpl;
import com.api.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Remove password from response
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequest request,
                                                Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User updatedUser = new User();
        updatedUser.setFirstName(request.getFirstName());
        updatedUser.setLastName(request.getLastName());
        updatedUser.setEmail(request.getEmail());
        updatedUser.setPhone(request.getPhone());

        User user = userService.updateUser(userDetails.getId(), updatedUser);
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Remove password from response
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }
}

