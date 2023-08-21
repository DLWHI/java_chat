package com.dlwhi.server.services;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.IUserRepository;

public class ChatService implements IUserService {
    private IUserRepository userRepo;

    public void setUserRepo(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return null;
        }
        return null;
    }

    @Override
    public User register(String username, String password) {
        return userRepo.save(new User(username, password));
    }
}
