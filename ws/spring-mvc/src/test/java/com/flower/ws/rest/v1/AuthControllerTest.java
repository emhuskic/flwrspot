package com.flower.ws.rest.v1;

import com.flower.knowledge.model.User;
import com.flower.util.exception.custom.InvalidCredentialsException;
import com.flower.ws.auth.params.LoginParams;
import com.flower.ws.auth.params.RegisterParams;
import com.flower.ws.auth.service.AuthService;
import com.flower.ws.rest.response.LoginResponse;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuthControllerTest {

    @Mock
    private final AuthService authService = EasyMock.createMock(AuthService.class);

    @TestSubject
    private final AuthController authController = new AuthController(authService);

    @Test
    public void test_registerUser() {
        final String username = "username";
        final String email = "em@ail.com";
        final String password = "password";
        final String token = "token";
        final RegisterParams registerParams = new RegisterParams(username, email, password);
        final User user = new User(UUID.randomUUID(), registerParams.getEmail(), registerParams.getUsername(), "encoded", Collections.emptyList());
        final LoginResponse loginResponse = new LoginResponse(user, token);

        EasyMock.expect(authService.register(registerParams)).andReturn(user);
        EasyMock.expect(authService.authenticate(username, password)).andReturn(token);
        EasyMock.replay(authService);

        final ResponseEntity<LoginResponse> response = authController.register(registerParams);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loginResponse.getToken(), response.getBody().getToken());
        assertEquals(loginResponse.getUser().getUsername(), response.getBody().getUser().getUsername());
        EasyMock.verify(authService);
    }

    @Test
    public void test_loginUser() {
        final String username = "username";
        final String email = "em@ail.com";
        final String password = "password";
        final String token = "token";
        final LoginParams loginParams = new LoginParams(username, password);
        final User user = new User(UUID.randomUUID(), email, username, "encoded", Collections.emptyList());
        final LoginResponse loginResponse = new LoginResponse(user, token);

        EasyMock.expect(authService.loadUserByUsername(username)).andReturn(user);
        EasyMock.expect(authService.authenticate(username, password)).andReturn(token);
        EasyMock.replay(authService);

        final ResponseEntity<?> response = authController.login(loginParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getClass().equals(LoginResponse.class));
        assertEquals(loginResponse.getToken(), ((LoginResponse)response.getBody()).getToken());
        assertEquals(loginResponse.getUser().getUsername(), ((LoginResponse)response.getBody()).getUser().getUsername());
        EasyMock.verify(authService);
    }

    @Test
    public void test_loginUser_userNotExists() {
        final String username = "username";
        final String password = "password";
        final LoginParams loginParams = new LoginParams(username, password);

        EasyMock.expect(authService.authenticate(username, password)).andThrow(new InvalidCredentialsException(""));
        EasyMock.replay(authService);

        final ResponseEntity<?> response = authController.login(loginParams);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        EasyMock.verify(authService);
    }
}
