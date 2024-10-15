package com.bookserver.deamon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.bookserver.deamon.model.User;
import com.bookserver.deamon.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public User register(User user) {
        // 加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            // 生成Token
            String token = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(token, user.getId(), 30, TimeUnit.MINUTES); // 设置有效期30分钟
            return token;
        }
        return null;
    }

    public boolean validateToken(String token) {
        return redisTemplate.hasKey(token);
    }

    public void logout(String token) {
        redisTemplate.delete(token);
    }

}
