package com.flower.ws.rest.v1;

import com.flower.knowledge.model.User;
import com.flower.ws.auth.service.AuthService;
import com.flower.ws.auth.params.LoginParams;
import com.flower.ws.auth.params.RegisterParams;
import com.flower.ws.rest.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody final RegisterParams registerRequest) {
        try {
            final User registeredUser = authService.register(registerRequest);
            final String jwtToken = authService.authenticate(registerRequest.getUsername(), registerRequest.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(registeredUser, jwtToken));
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final LoginParams loginRequest) {
        try {
            final String jwtToken = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            final User user = authService.loadUserByUsername(loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(user, jwtToken));
        } catch (final Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
