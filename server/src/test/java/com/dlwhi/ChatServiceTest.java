package com.dlwhi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dlwhi.server.models.User;
import com.dlwhi.server.repositories.TemplateUserRepository;
import com.dlwhi.server.services.ChatService;

public class ChatServiceTest {
    private TemplateUserRepository userRepo;
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

    @BeforeEach
    public void init(){
        userRepo = new TemplateUserRepository(
            new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.HSQL)
                .setScriptEncoding("UTF-8")
                .addScript("/schema.sql")
                .addScript("/data.sql")
                .build()
            );
    }

    @Test
    public void loginSuccess() {
        ChatService testSubject = new ChatService(userRepo, encoderMock);
        User logon = testSubject.login("Endel", "lxgiwyl;");
        assertNotNull(logon);
    }

    @Test
    public void loginWrongPasswd() {
        ChatService testSubject = new ChatService(userRepo, encoderMock);
        User logon = testSubject.login("Endel", "lxgwyl");
        assertNull(logon);
    }

    @Test
    public void loginWrongUser() {
        ChatService testSubject = new ChatService(userRepo, encoderMock);
        User logon = testSubject.login("Endl", "lxgiwyl");
        assertNull(logon);
    }

    @Test
    public void registerSuccess() {
        ChatService testSubject = new ChatService(userRepo);
        assertTrue(testSubject.register("me", "passwd"));
        User logon = testSubject.login("me", "passwd");
        assertNotNull(logon);
    }

    @Test
    public void registerExisting() {
        ChatService testSubject = new ChatService(userRepo);
        assertFalse(testSubject.register("John", "passwd"));
    }
}
