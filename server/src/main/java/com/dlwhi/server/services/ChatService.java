package com.dlwhi.server.services;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.UserRepository;

public class ChatService implements UserService {
    private final UserRepository userRepo;

    public ChatService(UserRepository userRepo) {
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
    public boolean register(String username, String password) {
        userRepo.save(new User(null, username, password));
        return true;
    }
}
