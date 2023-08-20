package edu.school21.server.repositories;

import edu.school21.server.models.Room;

public interface IRoomService extends ICrudRepository<Room> {
    Room findByName(String name);
}
