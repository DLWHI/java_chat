package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
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

    @ParameterizedTest(name = "Test finiding {0} | success")
    @ValueSource(longs = {0, 1, 2, 3, 4})
    public void findIdTestSuccess(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @ParameterizedTest(name = "Test finiding {0} | fail")
    @ValueSource(longs = {100, -1, 7, -10})
    public void findIdTestFail(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test finiding {0} | success")
    @ValueSource(strings = {"John", "Wayne", "Maxwell", "Endel", "Wyatt"})
    public void findUsernameTestSuccess(String username) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findByUsername(username);
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @ParameterizedTest(name = "Test finiding {0} | fail")
    @ValueSource(strings = {"", "jettec", "Endelw", "\0\0"})
    public void findUsernameTestFail(String username) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findByUsername(username);
        assertNull(found);
    }

    @Test
    public void findUsernameTestNull() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findByUsername(null);
        assertNull(found);
    }

    @Test
    public void saveTestSuccess() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertTrue(testSubject.save(new User(null, "white_coral", "_")));

        User found = testSubject.findByUsername("white_coral");
        assertNotNull(found);
        assertEquals(5, found.getId());
        assertEquals("white_coral", found.getUsername());
    }

    @Test
    public void saveTestExisting() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertFalse(testSubject.save(new User(null, "John", "_")));
    }

    @Test
    public void saveTestNull() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertThrows(NullPointerException.class, () -> testSubject.save(null));
    }

    @Test
    public void updateTestSuccess() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        User found = testSubject.findById(3l);
        found.setUsername("white_coral");
        assertTrue(testSubject.update(found));
        found = testSubject.findById(3l);
        assertNotNull(found);
        assertEquals(3, found.getId());
        assertEquals("white_coral", found.getUsername());
    }

    @Test
    public void updateTestNonExisting1() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertFalse(testSubject.update(new User(100l, "white_coral", "passwd")));
    }

    @Test
    public void updateTestNull() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertThrows(NullPointerException.class, () -> testSubject.update(null));
    }

    @ParameterizedTest(name = "Test deleting {0}")
    @ValueSource(longs = {0, 1, 2, 3, 4})
    public void deleteTestExisting(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        testSubject.delete(id);
        User found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test deleting {0}")
    @ValueSource(longs = {-1, 100, 232, 323, -323})
    public void deleteTestNonExisting(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        assertDoesNotThrow(() -> testSubject.delete(id));
    }

    @Test
    public void getAllTest() {
        TemplateUserRepository testSubject = new TemplateUserRepository(db);
        List<User> found = testSubject.getAll();
        assertNotNull(found);
        assertEquals(5, found.size());
    }
}
