package com.dlwhi.server.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.UserRepository;

public class ChatService implements UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public ChatService(UserRepository userRepo) {
        this.userRepo = userRepo;
        encoder = new BCryptPasswordEncoder();
    }

    public ChatService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(String username, String password) {
        return userRepo.save(new User(null, username, encoder.encode(password)));
    }
}
