package com.flower.knowledge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class Flower {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private List<FlowerSighting> flowerSightings;

    @JsonCreator
    public Flower(@JsonProperty("id") final UUID id,
                  @JsonProperty("name") final String name,
                  @JsonProperty("description") final String description,
                  @JsonProperty("imageUrl") final String imageUrl,
                  @JsonProperty("flowerSightings") final List<FlowerSighting> flowerSightings) {
        this(id, name, description, imageUrl, f -> flowerSightings);
    }

    public Flower(final UUID id,
                  final String name,
                  final String description,
                  final String imageUrl,
                  final Function<? super Flower, ? extends List<FlowerSighting>> sightingsProvider) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.flowerSightings = List.copyOf(
                Objects.requireNonNull(sightingsProvider.apply(this), "List of Sightings must not be null.")
        );
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @JsonIgnoreProperties("flower")
    public List<FlowerSighting> getFlowerSightings() {
        return flowerSightings;
    }
}
