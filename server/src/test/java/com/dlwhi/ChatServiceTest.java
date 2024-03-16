package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dlwhi.server.models.Room;
import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.TemplateMessageRepository;
import com.dlwhi.server.repositories.TemplateRoomRepository;
import com.dlwhi.server.repositories.TemplateUserRepository;
import com.dlwhi.server.services.ChatService;

public class ChatServiceTest {
    private TemplateUserRepository userRepo;
    private TemplateRoomRepository roomRepo;
    private TemplateMessageRepository msgRepo;
    private PasswordEncoder encoderMock;

    ChatServiceTest() {
        encoderMock = Mockito.mock(PasswordEncoder.class);
        Mockito
            .when(encoderMock.encode(anyString()))
            .thenAnswer(val -> val.getArguments()[0]);
        Mockito
            .when(encoderMock.matches(anyString(), anyString()))
            .thenAnswer(val -> val.getArguments()[0].equals(val.getArguments()[1]));
    }

    private static Stream<Arguments> userLoginProviderSuccess() {
        return Stream.of(
            Arguments.of("Endel", "lxgiwyl;"),  
            Arguments.of("Wayne", "1234")
        );
    }

    private static Stream<Arguments> userLoginProviderFail() {
        return Stream.of(
            Arguments.of("Endel", "lxgiwyl"),  
            Arguments.of("Wayn", "1234"),
            Arguments.of("Wayn", null),
            Arguments.of(null, "1234"),
            Arguments.of(null, null)
        );
    }

    private static Stream<Arguments> roomProviderSuccess() {
        return Stream.of(
            Arguments.of("null", 1l),  
            Arguments.of("null", null),  
            Arguments.of("", null) 
        );
    }

    private static Stream<Arguments> roomProviderFail() {
        return Stream.of(
            Arguments.of("null", -1l),  
            Arguments.of("null", 9l),  
            Arguments.of(null, null), 
            Arguments.of(null, 1l) 
        );
    }

    @BeforeEach
    public void init(){
        userRepo = new TemplateUserRepository(EmbeddedDBProvider.get());
        roomRepo = new TemplateRoomRepository(EmbeddedDBProvider.get());
        msgRepo = new TemplateMessageRepository(EmbeddedDBProvider.get());
    }

    @ParameterizedTest(name = "Test login with {0}:{1} | success")
    @MethodSource("userLoginProviderSuccess")
    public void loginSuccess() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo, encoderMock);
        User logon = testSubject.login("Endel", "lxgiwyl;");
        assertNotNull(logon);
    }

    @ParameterizedTest(name = "Test login with {0}:{1} | fail")
    @MethodSource("userLoginProviderFail")
    public void loginFail() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo, encoderMock);
        User logon = testSubject.login("Endel", "lxgwyl");
        assertNull(logon);
    }

    @Test
    public void registerSuccess() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo);
        assertTrue(testSubject.register("me", "passwd"));
        User logon = testSubject.login("me", "passwd");
        assertNotNull(logon);
    }

    @Test
    public void registerExisting() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo);
        assertFalse(testSubject.register("John", "passwd"));
    }

    @ParameterizedTest(name = "Test creating room {0} with owner {1} | success")
    @MethodSource("roomProviderSuccess")
    public void createRoomSuccess() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo);
        assertDoesNotThrow(() -> testSubject.createRoom("null", 1l));
        Room room = testSubject.findRoom("null").get(0);
        assertNotNull(room);
    }

    @ParameterizedTest(name = "Test creating room {0} with owner {1} | fail")
    @MethodSource("roomProviderFail")
    public void createRoomNonExistentOwnerFail() {
        ChatService testSubject = new ChatService(userRepo, roomRepo, msgRepo);
        assertDoesNotThrow(() -> testSubject.createRoom("null", -1l));
        Room room = testSubject.findRoom("null").get(0);
        assertNull(room);
    }
}
