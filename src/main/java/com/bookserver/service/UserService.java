package main.java.com.bookserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.bookserver.model.User;
import com.bookserver.repository.UserRepository;

@Sercice
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return user;
        }
        return null;
    }

}
