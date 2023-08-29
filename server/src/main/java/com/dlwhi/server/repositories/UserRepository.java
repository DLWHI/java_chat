package com.dlwhi.server.repositories;

import com.dlwhi.server.models.User;

public interface UserRepository extends CrudRepository<User> {
    User findByUsername(String username);
}
