package com.dlwhi.server.services;

import java.util.List;

import com.dlwhi.server.models.Room;

public interface RoomService {
    void createRoom(String name, Long owner);
    Room findRoom(long id);
    List<Room> findRoom(String roomName);
}
