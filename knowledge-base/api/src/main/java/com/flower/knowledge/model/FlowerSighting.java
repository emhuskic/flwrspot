package com.flower.knowledge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class FlowerSighting {

    private final UUID id;
    private final Double longitude;
    private final Double latitude;
    private final String imageUrl;
    private final User user;
    private final Flower flower;
    private final String quote;
    private final List<UserSightingLike> userLikes;

    @JsonCreator
    public FlowerSighting(@JsonProperty("id") final UUID id,
                          @JsonProperty("longitude") final Double longitude,
                          @JsonProperty("latitude") final Double latitude,
                          @JsonProperty("imageUrl") final String imageUrl,
                          @JsonProperty("user") final User user,
                          @JsonProperty("flower") final Flower flower,
                          @JsonProperty("quote") final String quote,
                          @JsonProperty("userLikes") final List<UserSightingLike> userLikes) {
        this(id, longitude, latitude, imageUrl, u -> user, flower, quote, u -> userLikes);
    }

    public FlowerSighting(final UUID id,
                          final Double longitude,
                          final Double latitude,
                          final String imageUrl,
                          final Function<? super FlowerSighting, ? extends User> userProvider,
                          final Flower flower,
                          final String quote,
                          final Function<? super FlowerSighting, ? extends List<UserSightingLike>> likesProvider) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.imageUrl = imageUrl;
        this.user = userProvider.apply(this);
        this.flower = flower;
        this.quote = quote;
        this.userLikes = List.copyOf(
                Objects.requireNonNull(likesProvider.apply(this), "List of user likes must not be null.")
        );

    }


    public UUID getId() {
        return id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @JsonIgnoreProperties("flowerSightings")
    public User getUser() {
        return user;
    }

    @JsonIgnoreProperties("flowerSightings")
    public Flower getFlower() {
        return flower;
    }

    public String getQuote() {
        return quote;
    }

    @JsonIgnoreProperties("flowerSighting")
    public List<UserSightingLike> getUserLikes() {
        return userLikes;
    }
}
