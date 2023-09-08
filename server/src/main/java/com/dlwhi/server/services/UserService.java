package com.dlwhi.server.services;

import com.dlwhi.server.models.User;

public interface UserService {
    public User login(String username, String password);
    public boolean register(String username, String password);
}
