package main.java.com.bookserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bookserver.model.User;

import main.java.com.bookserver.service.UserService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token, user.getId(), 30, TimeUnit.MINUTES);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        redisTemplate.delete(token);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
