package com.bookserver.deamon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import com.bookserver.deamon.model.User;
import com.bookserver.deamon.repository.UserRepository;

/**
 * 用户服务类，提供用户注册、登录、验证Token和登出功能。
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 注册用户。
     *
     * @param user 用户对象，包含用户名和密码。
     * @return 保存后的用户对象。
     */
    public User register(User user) {
        // 加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return userRepository.save(user);
    }

    /**
     * 用户登录。
     *
     * @param username 用户名。
     * @param password 密码。
     * @return 生成的Token，如果用户名或密码错误则返回null。
     */
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

    /**
     * 验证Token是否有效。
     *
     * @param token 要验证的Token。
     * @return 如果Token有效则返回true，否则返回false。
     */
    public boolean validateToken(String token) {
        return redisTemplate.hasKey(token);
    }

    /**
     * 用户登出。
     *
     * @param token 要删除的Token。
     */
    public void logout(String token) {
        redisTemplate.delete(token);
    }

    /**
     * 从 Token 中解析 userId
     *
     * @param token 用户的授权令牌
     * @return 解析出的用户ID
     */
    public String getUserIdFromToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

}