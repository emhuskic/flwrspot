package com.flower.ws.auth.service;
import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.User;
import com.flower.util.exception.custom.EmailInUseException;
import com.flower.util.exception.custom.InvalidCredentialsException;
import com.flower.ws.auth.JwtProvider;
import com.flower.ws.auth.params.RegisterParams;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AuthServiceTest {

    @Mock
    final PasswordEncoder passwordEncoder = EasyMock.createMock(PasswordEncoder.class);

    @Mock
    final KnowledgeBase knowledgeBase = EasyMock.createMock(KnowledgeBase.class);

    @Mock
    final JwtProvider jwtProvider = EasyMock.createMock(JwtProvider.class);

    @TestSubject
    final AuthService authService = new AuthService(passwordEncoder, knowledgeBase, jwtProvider);

    @Test
    public void test_registerUser() {
        final RegisterParams registerParams = new RegisterParams("username", "email", "password");
        final User user = new User(UUID.randomUUID(), registerParams.getEmail(), registerParams.getUsername(), "encoded", Collections.emptyList());

        EasyMock.expect(knowledgeBase.findUserByUsername(registerParams.getUsername())).andReturn(Optional.empty()).anyTimes();
        EasyMock.expect(knowledgeBase.findUserByEmail(registerParams.getEmail())).andReturn(Optional.empty());
        EasyMock.expect(knowledgeBase.save(EasyMock.anyObject(User.class))).andReturn(user);
        EasyMock.expect(passwordEncoder.encode(registerParams.getPassword())).andReturn("encoded");
        EasyMock.replay(knowledgeBase, passwordEncoder);

        final User registered = authService.register(registerParams);

        assertEquals(user.getUsername(), registered.getUsername());
        assertEquals(user.getPassword(), registered.getPassword());
        EasyMock.verify(knowledgeBase, passwordEncoder);
        EasyMock.resetToDefault(knowledgeBase, passwordEncoder);
    }

    @Test
    public void test_registerUser_inUse() {
        EasyMock.resetToDefault(knowledgeBase);
        final RegisterParams registerParams = new RegisterParams("username", "email", "password");
        final User user = new User(UUID.randomUUID(), registerParams.getEmail(), registerParams.getUsername(), "encoded", Collections.emptyList());

        EasyMock.expect(knowledgeBase.findUserByUsername(registerParams.getUsername())).andReturn(Optional.of(user)).anyTimes();
        EasyMock.replay(knowledgeBase);

        assertThrows(EmailInUseException.class, () -> authService.register(registerParams));
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase, passwordEncoder);
    }

    @Test
    public void test_authenticateUser() {
        EasyMock.resetToDefault();
        final String username = "username";
        final String password = "password";
        final User user = new User(UUID.randomUUID(), username, username, "password", Collections.emptyList());
        EasyMock.expect(knowledgeBase.findUserByUsername(username)).andReturn(Optional.of(user));
        EasyMock.expect(passwordEncoder.matches(password, user.getPassword())).andReturn(true);
        EasyMock.expect(jwtProvider.generateToken(user)).andReturn("token");

        EasyMock.replay(knowledgeBase, passwordEncoder, jwtProvider);

        final String resultToken = authService.authenticate(username, password);

        assertEquals(resultToken, "token");
        EasyMock.verify(knowledgeBase, passwordEncoder, jwtProvider);
        EasyMock.resetToDefault(knowledgeBase, passwordEncoder, jwtProvider);
    }

    @Test
    public void test_loadUserByUsername() {
        EasyMock.resetToDefault();
        final String username = "username";
        final User user = new User(UUID.randomUUID(), username, username, "password", Collections.emptyList());
        EasyMock.expect(knowledgeBase.findUserByUsername(username)).andReturn(Optional.of(user)).anyTimes();
        EasyMock.replay(knowledgeBase);

        final User result = authService.loadUserByUsername(username);

        assertEquals(result.getUsername(), user.getUsername());
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase);
    }

    @Test
    public void test_loadUserByUsername_notExists() {
        EasyMock.resetToDefault();
        final String username = "username";
        EasyMock.expect(knowledgeBase.findUserByUsername(username)).andReturn(Optional.empty()).anyTimes();
        EasyMock.replay(knowledgeBase);
        assertThrows(InvalidCredentialsException.class, () -> authService.loadUserByUsername(username));
        EasyMock.verify(knowledgeBase);
        EasyMock.resetToDefault(knowledgeBase);
    }
}
