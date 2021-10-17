package com.flower.knowledge;

import com.flower.knowledge.model.Flower;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KnowledgeBase {

    // USERS

    User save(final User user);

    Optional<User> findUserByUsername(final String username);

    Optional<User> findUserByEmail(final String email);

    // FLOWERS

    Optional<Flower> findFlowerByName(final String name);

    List<Flower> getAllFlowers(final int pageNumber, final int pageSize);

    // SIGHTINGS

    Optional<FlowerSighting> findSightingById(final UUID sightingId);

    List<FlowerSighting> getSightingsForUser(final UUID userId);

    List<FlowerSighting> getSightingsForFlower(final UUID flowerId);

    FlowerSighting saveSighting(final FlowerSighting sighting);

    void deleteSighting(final UUID sightingId);

    void deleteUserSightings(final UUID userId);

    void deleteUserSightings(final UUID userId, final UUID sightingId);

    // LIKES

    Optional<UserSightingLike> findLike(final UUID id);

    UserSightingLike saveLike(final UserSightingLike like);

    void deleteLike(final UUID likeId);

    void deleteUserLikes(final UUID userId);

    void deleteUserLikes(final UUID userId, final UUID likeId);


}
