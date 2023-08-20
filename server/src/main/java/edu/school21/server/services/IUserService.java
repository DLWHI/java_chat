package edu.school21.server.services;

import edu.school21.server.models.User;

public interface IUserService {
    public User login(String username, String password);
    public User register(String username, String password);
    public void logout(User user);
}
