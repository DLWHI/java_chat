package com.dlwhi.server.repositories;

import com.dlwhi.server.models.User;

public interface IUserRepository extends ICrudRepository<User> {
    User findByUsername(String username);
}
