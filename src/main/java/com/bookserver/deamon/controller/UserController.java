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

    /**
     * 注册新用户。
     *
     * @param user 要注册的用户
     * @return 如果注册成功，返回包含成功消息的 ResponseEntity；如果注册失败，返回包含错误消息的 ResponseEntity
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        if (registeredUser != null) {
            return ResponseEntity.ok("用户注册成功。");
        }
        return ResponseEntity.badRequest().body("注册失败。");
    }

    /**
     * 用户登录。
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功，返回包含令牌的 ResponseEntity；如果登录失败，返回包含错误消息的 ResponseEntity
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("凭证无效。");
    }

    /**
     * 验证令牌。
     *
     * @param token 要验证的令牌
     * @return 返回包含令牌是否有效的布尔值的 ResponseEntity
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        boolean isValid = userService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * 用户登出。
     *
     * @param token 要登出的用户的令牌
     * @return 返回包含成功消息的 ResponseEntity
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("登出成功。");
    }
}