package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.User;

public class TemplateUserRepository implements UserRepository {

    private final String findQuery =
        "select * from users where id = :users.id";
    private final String findUNameQuery =
        "select * from users where username = :users.username";
    private final String insertQuery =
        "insert into users(username, password) " +
        "values(:users.username, :users.password) " +
        "returning users.id, users.username, users.password;";
    private final String updateQuery = 
        "update users set username = ?, password = ? where id = ?";
    private final String deleteQuery =
        "delete from users where id = ?";
    private final String findAllQuery =
        "select * from users";

    private DataSource db;

    public TemplateUserRepository(DataSource db) {
        this.db = db;
    }

    @Override
    public User findById(Long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        User found = null;
        try {
            found = query.queryForObject(
            findQuery,
            new MapSqlParameterSource("users.id", id),
            new ModelRowMapper<User>(User.class)
        );
        } catch (DataAccessException e) {
        }
        return found;
    }

    @Override
    public List<User> findAll() {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            findAllQuery,
            new ModelRowMapper<User>(User.class)
        );
    }

    @Override
    public User save(User entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        User inserted = null;
        try {
            inserted = query.queryForObject(
                insertQuery,
                entity.getParamSource(),
                new ModelRowMapper<User>(User.class)
            );
        } catch (DataAccessException e) {
        }
        return inserted;
    }

    @Override
    public void update(User entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(updateQuery, entity.getParamSource());
    }

    @Override
    public void delete(Long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(deleteQuery, new MapSqlParameterSource("users.id", id));
    }

    @Override
    public User findByUsername(String username) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        User found = null;
        try {
            found = query.queryForObject(
            findUNameQuery,
            new MapSqlParameterSource("users.username", username),
            new ModelRowMapper<User>(User.class)
        );
        } catch (DataAccessException e) {
        }
        return found;
    }
    
}
