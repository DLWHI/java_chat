package com.dlwhi.server.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.dlwhi.server.models.User;

public class TemplateUserRepository implements UserRepository {

    private final String findQuery = "select * from users where id = :users.id";
    private final String findUNameQuery = "select * from users where username = :users.un";
    private final String insertQuery = "insert into " + 
        "users(username, password) values(:users.un, :users.passwd)";
    private final String updateQuery = "update users set " +
        "username = :users.un, password = :users.passwd where id = :users.id";
    private final String deleteQuery = "delete from users where id = :users.id;";
    private final String findAll = "select * from users";

    private DataSource DB;

    public TemplateUserRepository(DataSource dB) {
        DB = dB;
    }

    @Override
    public User findById(Long id) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(findQuery);
            query.setLong(1, id);
            ResultSet queryResult =  query.executeQuery();
            if (queryResult.next()) {
                return User.fromResultSet(queryResult);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> result = new LinkedList<>();
        try (Connection conn = DB.getConnection()) {
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public User save(User entity) {
        try (Connection conn = DB.getConnection()) {
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    @Override
    public void update(User entity) {
        try (Connection conn = DB.getConnection()) {
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(deleteQuery);
            query.setLong(1, id);
            query.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public User findByUsername(String email) {
        try (Connection conn = DB.getConnection()) {
            PreparedStatement query = conn.prepareStatement(findUNameQuery);
            query.setString(1, email);
            ResultSet queryResult =  query.executeQuery();
            if (queryResult.next()) {
                return User.fromResultSet(queryResult);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
