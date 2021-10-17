package com.flower.knowledge.entity;

import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_like")
public class UserSightingLikeEntity implements ModelBasedEntity<UserSightingLike> {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "sighting_id", referencedColumnName = "id")
    @NotNull
    private FlowerSightingEntity flowerSighting;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(final UserEntity user) {
        this.user = user;
    }

    public FlowerSightingEntity getFlowerSighting() {
        return flowerSighting;
    }

    public void setFlowerSighting(final FlowerSightingEntity flowerSightingEntity) {
        this.flowerSighting = flowerSightingEntity;
    }

    @Override
    public UserSightingLike toDomainModel() {
        return toDomainModel(flowerSighting.toDomainModel());
    }

    public UserSightingLike toDomainModel(final User user) {
        return toDomainModel(flowerSighting.toDomainModel(), user);
    }

    public UserSightingLike toDomainModel(final FlowerSighting sighting) {
        return toDomainModel(sighting, user.toDomainModel());
    }

    public UserSightingLike toDomainModel(final FlowerSighting sighting, final User user) {
        return new UserSightingLike(id, user, sighting);
    }


    public static UserSightingLikeEntity fromDomainModel(final UserSightingLike like) {
        return fromDomainModel(like, UserEntity.fromDomainModel(like.getUser()), FlowerSightingEntity.fromDomainModel(like.getFlowerSighting()));
    }

    public static UserSightingLikeEntity fromDomainModel(final UserSightingLike like, final UserEntity userEntity) {
        return fromDomainModel(like, userEntity, FlowerSightingEntity.fromDomainModel(like.getFlowerSighting()));
    }

    public static UserSightingLikeEntity fromDomainModel(final UserSightingLike like,
                                                         final UserEntity userEntity,
                                                         final FlowerSightingEntity flowerSightingEntity) {
        final UserSightingLikeEntity likeEntity = new UserSightingLikeEntity();
        likeEntity.setId(like.getId());
        likeEntity.setUser(userEntity);
        likeEntity.setFlowerSighting(flowerSightingEntity);
        return likeEntity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserSightingLikeEntity userFlowerLikeEntity = (UserSightingLikeEntity) o;
        return Objects.equals(user, userFlowerLikeEntity.user) &&
                Objects.equals(flowerSighting, userFlowerLikeEntity.flowerSighting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),
                user,
                flowerSighting
        );
    }
}
