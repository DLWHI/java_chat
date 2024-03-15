package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.TemplateUserRepository;

public class TemplateUserRepoTest {

    private static Stream<User> newUserProvider() {
        return Stream.of(
            new User(null, "someone", "sm"),
            new User(7l, "someone", "sm"),
            new User(null, "", ""),
            new User(null, "", "lxgiwyl;"),
            new User(null, " = null; drop db users", "null"),
            new User(1l, "someone", "sm")
        );
    }

    private static Stream<User> updateUserProvider() {
        return Stream.of(
            new User(1l, "someone", "sm"),
            new User(1l, "Wayne", "sm"),
            new User(3l, "Endel", ""),
            new User(2l, "", "lxgiwyl;"),
            new User(3l, " = null; drop db users", "null")
        );
    }

    private static Stream<User> failUserProvider() {
        return Stream.of(
            new User(null, "John", "sm"),
            new User(null, null, "sm"),
            new User(null, "null", null),
            new User(null, null, null)
        );
    }

    @ParameterizedTest(name = "Test finiding userID {0} | success")
    @ValueSource(longs = { 0, 1, 2, 3, 4 })
    public void findIdTestSuccess(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        User found = testSubject.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @ParameterizedTest(name = "Test finiding userID {0} | fail")
    @ValueSource(longs = { 100, -1, 7, -10 })
    public void findIdTestFail(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        User found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test finiding user {0} | success")
    @ValueSource(strings = { "John", "Wayne", "Maxwell", "Endel", "Wyatt" })
    public void findUsernameTestSuccess(String username) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        User found = testSubject.findByUsername(username);
        assertNotNull(found);
        assertEquals(username, found.getUsername());
    }

    @ParameterizedTest(name = "Test finiding user {0} | fail")
    @NullAndEmptySource
    @ValueSource(strings = { "", "jettec", "Endelw", "\0\0" })
    public void findUsernameTestFail(String username) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        User found = testSubject.findByUsername(username);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test inserting {0} | success")
    @MethodSource("newUserProvider")
    public void saveTestSuccess(User user) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertTrue(testSubject.save(user));

        User found = testSubject.findByUsername(user.getUsername());
        assertNotNull(found);
        assertEquals(5, found.getId());
        assertEquals(user.getUsername(), found.getUsername());
    }

    @ParameterizedTest(name = "Test inserting {0} | fail")
    @MethodSource("failUserProvider")
    public void saveTestFail(User user) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.save(user));
    }

    @Test
    public void saveTestNull() {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertThrows(NullPointerException.class, () -> testSubject.save(null));
    }

    @ParameterizedTest(name = "Test updating {0} | success")
    @MethodSource("updateUserProvider")
    public void updateTestSuccess(User user) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        user.setUsername("white_coral");
        assertTrue(testSubject.update(user));
        User found = testSubject.findById(user.getId());
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
        assertEquals(user.getUsername(), found.getUsername());
    }

    @ParameterizedTest(name = "Test updating {0} | fail")
    @MethodSource({"failUserProvider"})
    public void updateTestFail(User user) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.update(user));
    }

    @Test
    public void updateTestNull() {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertThrows(NullPointerException.class, () -> testSubject.update(null));
    }

    @ParameterizedTest(name = "Test deleting userID {0}")
    @ValueSource(longs = { 0, 1, 2, 3, 4 })
    public void deleteTestExisting(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        testSubject.delete(id);
        User found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test deleting userID {0}")
    @ValueSource(longs = { -1, 100, 232, 323, -323 })
    public void deleteTestNonExisting(long id) {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        assertDoesNotThrow(() -> testSubject.delete(id));
    }

    @Test
    public void getAllTest() {
        TemplateUserRepository testSubject = new TemplateUserRepository(EmbeddedDBProvider.get());
        List<User> found = testSubject.getAll();
        assertNotNull(found);
        assertEquals(5, found.size());
    }
}
