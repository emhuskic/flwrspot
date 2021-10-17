package com.flower.knowledge.entity;

import com.flower.knowledge.model.Flower;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "flower")
public class FlowerEntity implements ModelBasedEntity<Flower> {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String imageUrl;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "flower")
    @Fetch(FetchMode.SUBSELECT)
    private List<FlowerSightingEntity> flowerSightings;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<FlowerSightingEntity> getFlowerSightings() {
        return flowerSightings;
    }

    public void setFlowerSightings(final List<FlowerSightingEntity> flowerSightings) {
        this.flowerSightings = flowerSightings;
    }

    @Override
    public Flower toDomainModel() {
        return new Flower(
                id,
                name,
                description,
                imageUrl,
                flower -> flowerSightings
                        .stream()
                        .map(sighting -> sighting.toDomainModel(flower))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public static FlowerEntity fromDomainModel(final Flower flower) {
        final FlowerEntity flowerEntity = new FlowerEntity();
        flowerEntity.setId(flower.getId());
        flowerEntity.setName(flower.getName());
        flowerEntity.setDescription(flower.getDescription());
        flowerEntity.setImageUrl(flower.getImageUrl());

        if (flower.getFlowerSightings() != null) {
            flowerEntity.setFlowerSightings(flower.getFlowerSightings().stream().map(sighting -> FlowerSightingEntity.fromDomainModel(sighting, flowerEntity)).collect(Collectors.toUnmodifiableList()));
        }
        return flowerEntity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FlowerEntity flowerEntity = (FlowerEntity) o;
        return Objects.equals(name, flowerEntity.name) &&
                Objects.equals(description, flowerEntity.description) &&
                Objects.equals(imageUrl, flowerEntity.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                name,
                description,
                imageUrl
        );
    }


}
