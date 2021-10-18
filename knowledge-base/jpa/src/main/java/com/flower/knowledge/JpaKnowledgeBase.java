package com.flower.knowledge;

import com.flower.knowledge.entity.FlowerEntity;
import com.flower.knowledge.entity.FlowerSightingEntity;
import com.flower.knowledge.entity.UserEntity;
import com.flower.knowledge.entity.UserSightingLikeEntity;
import com.flower.knowledge.model.Flower;
import com.flower.knowledge.model.FlowerSighting;
import com.flower.knowledge.model.User;
import com.flower.knowledge.model.UserSightingLike;
import com.flower.knowledge.repository.FlowerRepository;
import com.flower.knowledge.repository.FlowerSightingRepository;
import com.flower.knowledge.repository.UserRepository;
import com.flower.knowledge.repository.UserSightingLikeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
public class JpaKnowledgeBase implements KnowledgeBase {

    private final UserRepository userRepository;
    private final FlowerRepository flowerRepository;
    private final FlowerSightingRepository sightingRepository;
    private final UserSightingLikeRepository likeRepository;

    public JpaKnowledgeBase(final UserRepository userRepository,
                            final FlowerRepository flowerRepository,
                            final FlowerSightingRepository sightingRepository,
                            final UserSightingLikeRepository likeRepository) {

        Objects.requireNonNull(userRepository, "User Repo must not be null");
        Objects.requireNonNull(flowerRepository, "Flower Repo must not be null");
        Objects.requireNonNull(sightingRepository, "Sighting Repo must not be null");
        Objects.requireNonNull(likeRepository, "Like Repo must not be null");

        this.userRepository = userRepository;
        this.flowerRepository = flowerRepository;
        this.sightingRepository = sightingRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public User save(final User user) {
        return this.userRepository.save(UserEntity.fromDomainModel(user)).toDomainModel();
    }

    @Override
    public Optional<User> findUserByUsername(final String username) {
        return this.userRepository.findByUsername(username).map(UserEntity::toDomainModel);
    }

    @Override
    public Optional<User> findUserByEmail(final String email) {
        return this.userRepository.findByEmail(email).map(UserEntity::toDomainModel);
    }

    @Override
    public Optional<Flower> findFlowerByName(final String name) {
        return this.flowerRepository.findByName(name).map(FlowerEntity::toDomainModel);
    }

    @Override
    public List<Flower> getAllFlowers(final int pageNumber, final int pageSize) {
        return this.flowerRepository.findAll(PageRequest.of(pageNumber - 1, pageSize))
                .map(FlowerEntity::toDomainModel)
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<FlowerSighting> findSightingById(final UUID sightingId) {
        return this.sightingRepository.findById(sightingId).map(FlowerSightingEntity::toDomainModel);
    }

    @Override
    public List<FlowerSighting> getSightingsForUser(final UUID userId) {
        return sightingRepository.findAllByUserId(userId).stream().map(FlowerSightingEntity::toDomainModel).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<FlowerSighting> getSightingsForFlower(final UUID flowerId) {
        return this.sightingRepository.findAllByFlowerId(flowerId).stream().map(FlowerSightingEntity::toDomainModel).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public FlowerSighting saveSighting(final FlowerSighting sighting) {
        return this.sightingRepository.save(FlowerSightingEntity.fromDomainModel(sighting)).toDomainModel();
    }

    @Override
    public void deleteSighting(final UUID sightingId) {
        this.sightingRepository.deleteById(sightingId);
    }

    @Override
    public void deleteUserSightings(final UUID userId) {
        this.sightingRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteUserSightings(final UUID userId, final UUID sightingId) {
        this.sightingRepository.deleteByIdAndUserId(userId, sightingId);
    }

    @Override
    public Optional<UserSightingLike> findLike(final UUID id) {
        return this.likeRepository.findById(id).map(UserSightingLikeEntity::toDomainModel);
    }

    @Override
    public UserSightingLike saveLike(final UserSightingLike like) {
        return this.likeRepository.save(UserSightingLikeEntity.fromDomainModel(like)).toDomainModel();
    }

    @Override
    public void deleteLike(final UUID likeId) {
        this.likeRepository.deleteById(likeId);
    }

    @Override
    public void deleteUserLikes(final UUID userId) {
        this.likeRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteUserLikes(final UUID userId, final UUID likeId) {
        this.likeRepository.deleteByIdAndUserId(likeId, userId);
    }
}
