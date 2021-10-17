package com.flower.knowledge.entity;

import com.flower.knowledge.model.Flower;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_flower_sighting")
public class FlowerSightingEntity implements ModelBasedEntity<FlowerSighting> {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Column
    private Double longitude;

    @Column
    private Double latitude;

    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "flower_id", referencedColumnName = "id")
    @NotNull
    private FlowerEntity flower;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "flowerSighting")
    @Fetch(FetchMode.SUBSELECT)
    private List<UserSightingLikeEntity> userLikes;

    @Column
    private String quote;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(final UserEntity user) {
        this.user = user;
    }

    public FlowerEntity getFlower() {
        return flower;
    }

    public void setFlower(final FlowerEntity flower) {
        this.flower = flower;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(final String quote) {
        this.quote = quote;
    }

    public List<UserSightingLikeEntity> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(final List<UserSightingLikeEntity> userLikes) {
        this.userLikes = userLikes;
    }

    @Override
    public FlowerSighting toDomainModel() {
        return toDomainModel(flower.toDomainModel());
    }

    public FlowerSighting toDomainModel(final Flower flower) {
        return new FlowerSighting(
                id,
                longitude,
                latitude,
                imageUrl,
                sighting -> user.toDomainModel(sighting),
                flower,
                quote,
                sighting -> userLikes.stream()
                        .map(like -> like.toDomainModel(sighting, user.toDomainModel(sighting)))
                        .collect(Collectors.toUnmodifiableList()));
    }


    public static FlowerSightingEntity fromDomainModel(final FlowerSighting flowerSighting) {
        return fromDomainModel(flowerSighting, FlowerEntity.fromDomainModel(flowerSighting.getFlower()));
    }

    public static FlowerSightingEntity fromDomainModel(final FlowerSighting flowerSighting, final FlowerEntity flowerEntity) {
        final FlowerSightingEntity flowerSightingEntity = new FlowerSightingEntity();

        flowerSightingEntity.setId(flowerSighting.getId());
        flowerSightingEntity.setLatitude(flowerSighting.getLatitude());
        flowerSightingEntity.setLongitude(flowerSighting.getLongitude());
        flowerSightingEntity.setImageUrl(flowerSighting.getImageUrl());
        flowerSightingEntity.setUser(UserEntity.fromDomainModel(flowerSighting.getUser(), flowerSightingEntity));
        flowerSightingEntity.setFlower(flowerEntity);
        flowerSightingEntity.setQuote(flowerSighting.getQuote());

        if (flowerSighting.getUserLikes() != null) {
            flowerSightingEntity.setUserLikes(flowerSighting
                    .getUserLikes()
                    .stream()
                    .map(like -> UserSightingLikeEntity
                            .fromDomainModel(like,
                                    UserEntity.fromDomainModel(flowerSighting.getUser(), flowerSightingEntity),
                                    flowerSightingEntity)
                    )
                    .collect(Collectors.toUnmodifiableList()));
        }

        return flowerSightingEntity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlowerSightingEntity flowerSightingEntity = (FlowerSightingEntity) o;
        return Objects.equals(latitude, flowerSightingEntity.latitude) &&
                Objects.equals(longitude, flowerSightingEntity.longitude) &&
                Objects.equals(imageUrl, flowerSightingEntity.imageUrl) &&
                Objects.equals(user, flowerSightingEntity.user) &&
                Objects.equals(flower, flowerSightingEntity.flower) &&
                Objects.equals(quote, flowerSightingEntity.quote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                latitude,
                longitude,
                imageUrl,
                user,
                flower,
                quote
        );
    }

}
