package com.flower.knowledge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flower.knowledge.model.FlowerSighting;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_account")
public class UserEntity implements ModelBasedEntity<com.flower.knowledge.model.User> {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @Fetch(FetchMode.SUBSELECT)
    private List<FlowerSightingEntity> flowerSightings;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @Fetch(FetchMode.SUBSELECT)
    private List<UserSightingLikeEntity> userLikes;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
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

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public User toDomain() {
        return new User(this.username, this.password, null);
    }

    public List<FlowerSightingEntity> getFlowerSightings() {
        return flowerSightings;
    }

    public void setFlowerSightings(final List<FlowerSightingEntity> flowerSightings) {
        this.flowerSightings = flowerSightings;
    }

    public List<UserSightingLikeEntity> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(final List<UserSightingLikeEntity> userLikes) {
        this.userLikes = userLikes;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserEntity user = (UserEntity) o;
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
    public com.flower.knowledge.model.User toDomainModel() {
        return new com.flower.knowledge.model.User(
                this.id,
                this.email,
                this.username,
                this.password,
                user -> userLikes.stream()
                        .map(like -> like.toDomainModel(user))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public com.flower.knowledge.model.User toDomainModel(final FlowerSighting sighting) {
        return new com.flower.knowledge.model.User(
                this.id,
                this.email,
                this.username,
                this.password,
                user -> userLikes.stream()
                        .map(like -> like.toDomainModel(sighting, user))
                        .collect(Collectors.toUnmodifiableList())
        );
    }


    public static UserEntity fromDomainModel(final com.flower.knowledge.model.User user) {
        final UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());

        if (user.getUserLikes() != null) {
            userEntity.setUserLikes(user.getUserLikes().stream().map(like -> UserSightingLikeEntity.fromDomainModel(like, userEntity)).collect(Collectors.toUnmodifiableList()));
        }

        return userEntity;
    }

    public static UserEntity fromDomainModel(final com.flower.knowledge.model.User user, final FlowerSightingEntity flowerSightingEntity) {
        final UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());

        if (user.getUserLikes() != null) {
            userEntity.setUserLikes(user.getUserLikes()
                    .stream()
                    .map(like -> UserSightingLikeEntity.fromDomainModel(like, userEntity, flowerSightingEntity))
                    .collect(Collectors.toUnmodifiableList()));
        }

        return userEntity;
    }
}
