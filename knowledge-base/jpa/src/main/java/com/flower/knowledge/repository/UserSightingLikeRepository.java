package com.flower.knowledge.repository;

import com.flower.knowledge.entity.UserSightingLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface UserSightingLikeRepository extends JpaRepository<UserSightingLikeEntity, UUID> {

    UserSightingLikeEntity save(final UserSightingLikeEntity entity);

    void deleteById(final UUID id);

    void deleteByUserId(final UUID userId);

    void deleteByIdAndUserId(final UUID id, final UUID userId);
}
