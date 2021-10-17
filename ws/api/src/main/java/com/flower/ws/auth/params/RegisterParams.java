package com.flower.ws.auth.params;

import com.flower.util.email.ValidEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegisterParams {
    @NotBlank(message = "Username shouldn't be blank")
    private String username;

    @ValidEmail(message = "Provide a valid email address")
    private String email;

    @NotNull(message = "Password must be present")
    @Size(min = 5, message = "Password should contain at least 5 characters")
    private String password;

    public RegisterParams() {
    }

    public RegisterParams(final String username, final String email, final String password) {
        Objects.requireNonNull(username, "Username should be provided!");
        Objects.requireNonNull(email, "Email should be provided!");
        Objects.requireNonNull(password, "Password should be provided!");

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}