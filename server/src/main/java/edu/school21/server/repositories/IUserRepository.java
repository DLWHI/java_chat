package edu.school21.server.repositories;

import edu.school21.server.models.User;

public interface IUserRepository extends ICrudRepository<User> {
    User findByUsername(String username);
}
