package com.dlwhi.server.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dlwhi.server.models.Message;
import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.MessageRepository;
import com.dlwhi.server.repositories.RoomRepository;
import com.dlwhi.server.repositories.UserRepository;

public class ChatService implements UserService, RoomService, MessageService {
    private final UserRepository userRepo;
    private final RoomRepository roomRepo;
    private final MessageRepository msgRepo;
    private final PasswordEncoder encoder;

    public ChatService(
        UserRepository userRepo,
        RoomRepository roomRepo,
        MessageRepository msgRepo
    ) {
        this(userRepo, roomRepo, msgRepo, new BCryptPasswordEncoder());
    }

    public ChatService(
        UserRepository userRepo, 
        RoomRepository roomRepo, 
        MessageRepository msgRepo, 
        PasswordEncoder encoder
    ) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
        this.msgRepo = msgRepo; 
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
    public void createRoom(String name, Long owner) {
        roomRepo.save(new Room(null, name, owner));
    }

    @Override
    public Room findRoom(long id) {
        return roomRepo.findById(id);
    }

    @Override
    public List<Room> findRoom(String roomName) {
        return roomRepo.findByName(roomName);
    }

    @Override
    public void createMessage(String text, long roomId, User author) {
        msgRepo.save(new Message(null, text, author, roomId));

    }

    @Override
    public List<Message> lastInRoom(int count, long roomId) {
        return msgRepo.getLastInRoom(roomId, count);
    }
}
