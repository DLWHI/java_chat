package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.dlwhi.server.models.Message;
import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.TemplateMessageRepository;

public class TemplateMessageRepoTest {
    private static Stream<Message> saveSuccessMessageProvider() {
        return Stream.of(
            new Message(null, "text", new User(1l, null, null), 1l),
            new Message(1l, "text", new User(1l, null, null), 1l),
            new Message(-1l, "text", new User(1l, null, null), 1l),
            new Message(null, "some really long fucking text", new User(1l, null, null), 1l)
        );
    }

    private static Stream<Message> saveFailMessageProvider() {
        return Stream.of(
            new Message(null, "text", null, 1l),
            new Message(null, null, null, 1l),
            new Message(null, "txt", null, 1l),
            new Message(null, null, new User(1l, null, null), 1l),
            new Message(null, null, null, null),
            new Message(null, "text", null, null),
            new Message(null, new String(new char[512]).replace("\0", "a"), null, null)
        );
    }

    private static Stream<Message> updateSuccessMessageProvider() {
        return Stream.of(
            new Message(1l, "room", null, null),
            new Message(2l, "room", null, 1l),
            new Message(2l, "room", new User(), null),
            new Message(2l, "", null, null)
            );
        }
        
        private static Stream<Message> updateFailMessageProvider() {
            return Stream.of(
            new Message(-1l, "room", null, null),
            new Message(100l, "room", null, null),
            new Message(null, null, new User(), 1l),
            new Message(null, "null", new User(), 1l),
            new Message(1l, null, null, 1l),
            new Message(null, null, new User(), 1l)
        );
    }

    @ParameterizedTest(name = "Test finiding messageID {0} | success")
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 6, 7})
    public void findIdTestSuccess(long id) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        Message found = testSubject.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @ParameterizedTest(name = "Test finiding messageID {0} | fail")
    @ValueSource(longs = {100, -1, 8, -10})
    public void findIdTestFail(long id) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        Message found = testSubject.findById(id);
        assertNull(found);
    }

    @ParameterizedTest(name = "Test finiding messages in roomID {0} | success")
    @ValueSource(longs = {1, 2, 3})
    public void findInRoomTestSuccess(long roomId) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        List<Message> found = testSubject.getAllInRoom(roomId);
        assertNotNull(found);
        assertNotEquals(0, found.size());
        for (Message msg : found) {
            assertEquals(roomId, msg.getRoomId());
        }
    }

    @ParameterizedTest(name = "Test finiding messages in roomID {0} | fail")
    @ValueSource(longs = {0, 4, 100, -2, 34})
    public void findInRoomTestFail(long roomId) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        List<Message> found = testSubject.getAllInRoom(roomId);
        assertNotNull(found);
        assertEquals(0, found.size());
    }

    @ParameterizedTest(name = "Test finiding messageID {0}")
    @ValueSource(longs = {0, 1, 2, 3, 4, 5, 6, 7, 100, -2, 34})
    public void findLimitTest(long id) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        List<Message> found = testSubject.getLastInRoom(id, 30);
        assertNotNull(found);
        assertTrue(30 >= found.size());
    }

    @ParameterizedTest(name = "Test inserting {0} | success")
    @MethodSource("saveSuccessMessageProvider")
    public void saveTestSuccess(Message msg) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        assertTrue(testSubject.save(msg));
    }

    @ParameterizedTest(name = "Test inserting {0} | fail")
    @NullSource
    @MethodSource("saveFailMessageProvider")
    public void saveTestFail(Message msg) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.save(msg));
    }

    @ParameterizedTest(name = "Test updating {0} | success")
    @MethodSource("updateSuccessMessageProvider")
    public void updateTestSuccess(Message msg) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        assertTrue(testSubject.update(msg));
        Message found = testSubject.findById(msg.getId());
        assertNotNull(found);
        assertEquals(msg.getText(), msg.getText());
    }

    @ParameterizedTest(name = "Test updating {0} | fail")
    @NullSource
    @MethodSource({"updateFailMessageProvider"})
    public void updateTestFail(Message msg) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        assertFalse(testSubject.update(msg));
    }

    @ParameterizedTest(name = "Test deleting messageID {0}")
    @ValueSource(longs = {0, 1, 2, 3, -1, 100, 232, 323, -323})
    public void deleteTest(long id) {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        assertDoesNotThrow(() -> testSubject.delete(id));
        Message found = testSubject.findById(id);
        assertNull(found);
    }

    @Test
    public void getAllTest() {
        TemplateMessageRepository testSubject = new TemplateMessageRepository(EmbeddedDBProvider.get());
        List<Message> found = testSubject.getAll();
        assertNotNull(found);
        assertEquals(8, found.size());
    }
}
