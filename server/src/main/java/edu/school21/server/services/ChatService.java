package edu.school21.server.services;

import edu.school21.server.models.User;
import edu.school21.server.repositories.IUserRepository;

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
        } else if (user.isAuthed()) {
            // throw
        } else if (user.authenticate(password)) {
            userRepo.update(user);
            return user;
        }
        return null;
    }

    @Override
    public User register(String username, String password) {
        return userRepo.save(new User(username, password));
    }

    @Override
    public void logout(User user) {
        user.logout();
        userRepo.update(user);
    }    
}
