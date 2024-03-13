package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.TemplateUserRepository;

public class TemplateUserRepoTest {
    private EmbeddedDatabase db;
    
    @BeforeEach
    public void init(){
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .setScriptEncoding("UTF-8")
                .addScript("/schema.sql")
                .addScript("/data.sql")
                .build();
    }

    @ParameterizedTest(name = "Test finiding {0}")
    @ValueSource(longs = {0, 1, 2, 3, 4})
    public void findTest(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @ParameterizedTest(name = "Test finiding {0}")
    @ValueSource(strings = {"John", "Wayne", "Maxwell", "Endel", "Wyatt"})
    public void findUsernameTest(String username) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findByUsername(username);
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @Test
    public void saveTest() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        testSubject.save(new User(null, "white_coral", "_"));

        User found = testSubject.findByUsername("white_coral");
        assertNotNull(found);
        assertEquals(5, found.getId());
        assertEquals("white_coral", found.getUsername());
    }

    @Test
    public void updateTest() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findById(3l);
        found.setUsername("white_coral");
        testSubject.update(found);
        found = testSubject.findById(3l);
        assertNotNull(found);
        assertEquals(3, found.getId());
        assertEquals("white_coral", found.getUsername());
    }

    @ParameterizedTest(name = "Test deleting {0}")
    @ValueSource(longs = {0, 1, 2, 3, 4})
    public void deleteTest(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        testSubject.delete(id);
        User found = testSubject.findById(id);
        assertNull(found);
    }

    @Test
    public void getAllTest() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        List<User> found = testSubject.getAll();
        assertNotNull(found);
        assertEquals(5, found.size());
    }
}
