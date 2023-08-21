package com.dlwhi.server.services;

import com.dlwhi.server.models.User;

public interface IUserService {
    public User login(String username, String password);
    public User register(String username, String password);
}