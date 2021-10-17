package com.flower.ws.auth.params;

import javax.validation.constraints.NotBlank;

public class LoginParams {

    @NotBlank(message = "Username must be present")
    private String username;

    @NotBlank(message = "Password must be present")
    private String password;

    public LoginParams() {
    }

    public LoginParams(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(final String email) {
        this.username = email;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}