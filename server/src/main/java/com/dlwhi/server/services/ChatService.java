package com.dlwhi.server.services;

import org.springframework.stereotype.Component;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.UserRepository;

@Component
public class ChatService implements UserService {
    private UserRepository userRepo;

    public void setUserRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user != null && user.passwdMatches(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User register(String username, String password) {
        return userRepo.save(new User(username, password));
    }
}
