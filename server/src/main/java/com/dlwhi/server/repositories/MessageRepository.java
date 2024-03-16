package com.dlwhi.server.repositories;

import java.util.List;

import com.dlwhi.server.models.Message;

public interface MessageRepository extends CrudRepository<Message> {
    List<Message> getAllInRoom(long roomId);
    List<Message> getLastInRoom(long roomId, int count);
}
