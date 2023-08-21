package com.dlwhi.server.repositories;

import com.dlwhi.server.models.Room;

public interface IRoomService extends ICrudRepository<Room> {
    Room findByName(String name);
}
