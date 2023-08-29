package com.dlwhi.server.repositories;

import com.dlwhi.server.models.Room;

public interface RoomService extends CrudRepository<Room> {
    Room findByName(String name);
}
