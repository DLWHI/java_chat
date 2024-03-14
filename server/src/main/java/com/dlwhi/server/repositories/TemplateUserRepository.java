package com.dlwhi.server.repositories;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.dlwhi.server.models.User;

public class TemplateUserRepository implements UserRepository {
    private final String COLUMN_SELECT = 
        "id as users.id, " + 
        "username as users.username, " + 
        "password as users.password";

    private final String FIND_QUERY_ID =
        "select " + COLUMN_SELECT + " from users where id = :id;";
    private final String FIND_QUERY_USERNAME =
        "select " + COLUMN_SELECT + " from users where username = :user;";
    private final String INSERT_QUERY =
        "insert into users(username, password) " +
        "values(:user, :passwd);";
    private final String UPDATE_QUERY = 
        "update users set username = :user, password = :passwd where id = :id;";
    private final String DELETE_QUERY =
        "delete from users where id = :id;";
    private final String GET_ALL_QUERY =
        "select " + COLUMN_SELECT + " from users;";

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
                FIND_QUERY_ID,
                new MapSqlParameterSource("id", id),
                new BeanPropertyRowMapper<User>(User.class)
            );
        } catch (DataAccessException e) {
        }
        return found;
    }

    @Override
    public List<User> getAll() {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        return query.query(
            GET_ALL_QUERY,
            new BeanPropertyRowMapper<User>(User.class)
        );
    }

    @Override
    public void save(User entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        try {
            query.update(
                INSERT_QUERY,
                new MapSqlParameterSource("user", entity.getUsername())
                    .addValue("passwd", entity.getPassword())
            );
        } catch (DataAccessException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void update(User entity) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(
            UPDATE_QUERY, 
            new MapSqlParameterSource("id", entity.getId())
                .addValue("user", entity.getUsername())
                .addValue("passwd", entity.getPassword())
        );
    }

    @Override
    public void delete(Long id) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        query.update(DELETE_QUERY, new MapSqlParameterSource("id", id));
    }

    @Override
    public User findByUsername(String username) {
        NamedParameterJdbcTemplate query = new NamedParameterJdbcTemplate(db);
        User found = null;
        try {
            found = query.queryForObject(
            FIND_QUERY_USERNAME,
            new MapSqlParameterSource("user", username),
            new BeanPropertyRowMapper<User>(User.class)
        );
        } catch (DataAccessException e) {
        }
        return found;
    }
}
