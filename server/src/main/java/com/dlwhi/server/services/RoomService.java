package com.dlwhi.server.services;

import java.util.List;

import com.dlwhi.server.models.Room;

public interface RoomService {
    Room find(long id);
    List<Room> find(String roomName);
}
