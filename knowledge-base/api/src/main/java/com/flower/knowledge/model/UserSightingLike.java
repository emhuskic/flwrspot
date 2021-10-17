package com.flower.knowledge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class UserSightingLike {

    private final UUID id;
    private final User user;
    private final FlowerSighting flowerSighting;

    @JsonCreator
    public UserSightingLike(@JsonProperty("id") final UUID id,
                            @JsonProperty("user") final User user,
                            @JsonProperty("flowerSighting") final FlowerSighting flowerSighting) {
        this.id = id;
        this.user = user;
        this.flowerSighting = flowerSighting;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnoreProperties("userLikes")
    public User getUser() {
        return user;
    }

    @JsonIgnoreProperties({"user", "userLikes"})
    public FlowerSighting getFlowerSighting() {
        return flowerSighting;
    }
}
