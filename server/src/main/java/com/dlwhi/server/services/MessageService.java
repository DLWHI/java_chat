package com.dlwhi.server.services;

import java.util.List;

import com.dlwhi.server.models.Message;
import com.dlwhi.server.models.User;

public interface MessageService {
    void createMessage(String text, long roomId, User author);
    List<Message> lastInRoom(int count, long roomId);
}
