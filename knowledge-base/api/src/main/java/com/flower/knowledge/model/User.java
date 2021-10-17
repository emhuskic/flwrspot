package com.flower.knowledge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class User implements UserDetails {
    private UUID id;
    private String email;
    private String username;
    private String password;
    private List<UserSightingLike> userLikes;

    @JsonCreator
    public User(@JsonProperty("id") final UUID id,
                @JsonProperty("email") final String email,
                @JsonProperty("username") final String username,
                @JsonProperty("password") final String password,
                @JsonProperty("userLikes") final List<UserSightingLike> userLikes) {
        this(id, email, username, password, u -> userLikes);
    }

    public User(final UUID id,
                final String email,
                final String username,
                final String password,
                final Function<? super User, ? extends List<UserSightingLike>> likesProvider) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userLikes = List.copyOf(
                Objects.requireNonNull(likesProvider.apply(this), "List of user likes must not be null.")
        );
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonIgnoreProperties("user")
    public List<UserSightingLike> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(final List<UserSightingLike> userLikes) {
        this.userLikes = userLikes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                username,
                email
        );
    }


    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    // NULL by default for task needs, usually contains roles/permissions
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
