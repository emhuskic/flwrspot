package com.flower.ws.rest.response;

import com.flower.knowledge.model.User;

public class LoginResponse {
    private User user;
    private String token;

    public LoginResponse(final User user, final String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}