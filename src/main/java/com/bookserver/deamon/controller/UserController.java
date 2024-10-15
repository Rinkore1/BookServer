package com.bookserver.deamon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookserver.deamon.model.User;

import com.bookserver.deamon.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        if (registeredUser != null) {
            return ResponseEntity.ok("User registered successfully.");
        }
        return ResponseEntity.badRequest().body("Registration failed.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = userService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
