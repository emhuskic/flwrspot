package com.flower.ws.auth.service;

import com.flower.knowledge.KnowledgeBase;
import com.flower.knowledge.model.User;
import com.flower.util.exception.custom.EmailInUseException;
import com.flower.util.exception.custom.InvalidCredentialsException;
import com.flower.ws.auth.JwtProvider;
import com.flower.ws.auth.params.RegisterParams;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class AuthService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final KnowledgeBase knowledgeBase;
    private final JwtProvider jwtProvider;

    public AuthService(final PasswordEncoder passwordEncoder,
                       final KnowledgeBase knowledgeBase,
                       final JwtProvider jwtProvider) {
        Objects.requireNonNull(passwordEncoder, "Password encoder must not be null!");
        Objects.requireNonNull(knowledgeBase, "Knowledge base must not be null!");
        Objects.requireNonNull(jwtProvider, "Jwt provider must not be null!");

        this.passwordEncoder = passwordEncoder;
        this.knowledgeBase = knowledgeBase;
        this.jwtProvider = jwtProvider;
    }

    public User register(final RegisterParams registerRequest) {
        if (userExistsByUsername(registerRequest.getUsername()) || userExistsByEmail(registerRequest.getEmail())) {
            throw new EmailInUseException(String.format("%s is already in use.", registerRequest.getEmail()));
        }

        String password = passwordEncoder.encode(registerRequest.getPassword());
        final User user = new com.flower.knowledge.model.User(
                null,
                registerRequest.getEmail(),
                registerRequest.getUsername(),
                password,
                Collections.emptyList()
        );
        this.knowledgeBase.save(user);
        return user;
    }

    private boolean userExistsByUsername(final String username) {
        return knowledgeBase.findUserByUsername(username).isPresent();
    }

    private boolean userExistsByEmail(final String email) {
        return knowledgeBase.findUserByEmail(email).isPresent();
    }

    public User loadUserByEmail(final String email) {
        return knowledgeBase
                .findUserByEmail(email)
                .orElseThrow(
                        () -> new InvalidCredentialsException(String.format("Email %s isn't associated with an account", email))
                );
    }

    @Override
    public User loadUserByUsername(final String s) throws UsernameNotFoundException {
        return knowledgeBase
                .findUserByUsername(s)
                .orElseThrow(
                        () -> new InvalidCredentialsException(String.format("Username %s isn't associated with an account", s))
                );
    }

    public String authenticate(final String username, final String password) {
        final User user = loadUserByUsername(username);
        boolean correctPassword = passwordEncoder.matches(password, user.getPassword());
        if (!correctPassword) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                password,
                null
        );

        SecurityContextHolder.getContext().setAuthentication(token);
        return jwtProvider.generateToken(user);
    }
}