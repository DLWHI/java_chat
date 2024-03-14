package com.dlwhi.server.repositories;

import java.util.List;

import com.dlwhi.server.models.Room;

public interface RoomRepository extends CrudRepository<Room> {
    List<Room> findByName(String name);
}
