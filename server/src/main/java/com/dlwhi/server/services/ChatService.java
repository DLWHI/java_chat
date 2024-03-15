package com.dlwhi.server.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.RoomRepository;
import com.dlwhi.server.repositories.UserRepository;

public class ChatService implements UserService, RoomService {
    private final UserRepository userRepo;
    private final RoomRepository roomRepo;
    private final PasswordEncoder encoder;

    public ChatService(UserRepository userRepo, RoomRepository roomRepo) {
        this(userRepo, roomRepo, new BCryptPasswordEncoder());
    }

    public ChatService(UserRepository userRepo, RoomRepository roomRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo; 
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

    @Override
    public Room find(long id) {
        return roomRepo.findById(id);
    }

    @Override
    public List<Room> find(String roomName) {
        return roomRepo.findByName(roomName);
    }
}
