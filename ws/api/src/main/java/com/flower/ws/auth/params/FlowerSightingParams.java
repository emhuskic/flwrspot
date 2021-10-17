package com.flower.ws.auth.params;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flower.knowledge.model.Flower;

public class FlowerSightingParams {

    private final Double longitude;
    private final Double latitude;
    private final String imageUrl;
    private final Flower flower;

    public FlowerSightingParams() {
        this(null, null, null, null);
    }

    @JsonCreator
    public FlowerSightingParams(@JsonProperty("longitude") final Double longitude,
                                @JsonProperty("latitude") final Double latitude,
                                @JsonProperty("imageUrl") final String imageUrl,
                                @JsonProperty("flower") final Flower flower) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.imageUrl = imageUrl;
        this.flower = flower;
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

    public Flower getFlower() {
        return flower;
    }
}
