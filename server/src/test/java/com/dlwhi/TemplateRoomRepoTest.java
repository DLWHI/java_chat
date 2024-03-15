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
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.dlwhi.server.models.Room;
import com.dlwhi.server.repositories.TemplateRoomRepository;

public class TemplateRoomRepoTest {
    private static Stream<Arguments> roomDataProvider() {
        return Stream.of(
            Arguments.of("Domik ebli", 1),
            Arguments.of("Rofl and Dance school", 1),
            Arguments.of("untermenschi", 2),
            Arguments.of("kulturisti", 1)
        );
    }

    private static Stream<Room> newRoomProvider() {
        return Stream.of(
            new Room(null, "room", 1l),
            new Room(null, "room", null),
            new Room(null, "", null),
            new Room(null, "", 1l),
            new Room(null, "drop db rooms", 1l),
            new Room(1l, "drop db rooms", 1l)
        );
    }

    private static Stream<Room> updateRoomProvider() {
        return Stream.of(
            new Room(1l, "room", 0l),
            new Room(1l, "room", null),
            new Room(1l, "", null),
            new Room(1l, "", 1l),
            new Room(1l, "drop db rooms", 1l)
        );
    }

    private static Stream<Room> failRoomProvider() {
        return Stream.of(
            new Room(null, null, 0l),
            new Room(null, null, null)
        );
    }

    @ParameterizedTest(name = "Test finiding roomID {0} | success")
    @ValueSource(longs = {0, 1, 2, 3})
    public void findIdTestSuccess(long id) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        Room found = testSubject.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @ParameterizedTest(name = "Test finiding roomID {0} | fail")
    @ValueSource(longs = {100, -1, 7, -10})
    public void findIdTestFail(long id) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        Room found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test finiding room {0} | success")
    @MethodSource("roomDataProvider")
    public void findNameTestSuccess(String name, int count) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        List<Room> found = testSubject.findByName(name);
        assertNotNull(found);
        assertEquals(count, found.size());
        for (Room room : found) {
            assertEquals(name, room.getName());
        }
    }

    @ParameterizedTest(name = "Test finiding room {0} | fail")
    @NullAndEmptySource
    @ValueSource(strings = {"jettec", "Endelw"})
    public void findNameTestFail(String username) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        List<Room> found = testSubject.findByName(username);
        assertNotNull(found);
        assertEquals(0, found.size());
    }

    @ParameterizedTest(name = "Test inserting room {0} | success")
    @MethodSource("newRoomProvider")
    public void saveTestSuccess(Room room) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertTrue(testSubject.save(room));

        Room found = testSubject.findByName(room.getName()).get(0);
        assertEquals(5, found.getId());
        assertEquals(room.getName(), found.getName());
        if (room.getOwnerId() != null) {
            assertEquals(room.getOwnerId(), found.getOwnerId());
        }
    }

    @ParameterizedTest(name = "Test inserting user {0} | fail")
    @MethodSource("failRoomProvider")
    public void saveTestFail(Room room) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.save(room));
    }

    @Test
    public void saveTestNull() {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertThrows(NullPointerException.class, () -> testSubject.save(null));
    }

    @ParameterizedTest(name = "Test updating {0} | success")
    @MethodSource("updateRoomProvider")
    public void updateTestSuccess(Room room) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        room.setName("white_coral");
        assertTrue(testSubject.update(room));
        Room found = testSubject.findById(room.getId());
        assertNotNull(found);
        assertEquals(room.getName(), found.getName());
        if (room.getOwnerId() != null) {
            assertEquals(room.getOwnerId(), found.getOwnerId());
        }
    }

    @ParameterizedTest(name = "Test updating {0} | fail")
    @MethodSource({"failRoomProvider"})
    public void updateTestFail(Room room) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.update(room));
    }

    @Test
    public void updateTestNull() {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertThrows(NullPointerException.class, () -> testSubject.update(null));
    }

    @ParameterizedTest(name = "Test deleting roomID {0}")
    @ValueSource(longs = {0, 1, 2, 3})
    public void deleteTestExisting(long id) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        testSubject.delete(id);
        Room found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test deleting roomID {0}")
    @ValueSource(longs = {-1, 100, 232, 323, -323})
    public void deleteTestNonExisting(long id) {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        assertDoesNotThrow(() -> testSubject.delete(id));
    }

    @Test
    public void getAllTest() {
        TemplateRoomRepository testSubject = new TemplateRoomRepository(EmbeddedDBProvider.get());
        List<Room> found = testSubject.getAll();
        assertNotNull(found);
        assertEquals(5, found.size());
    }
}
