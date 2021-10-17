package com.flower.knowledge.repository;

import com.flower.knowledge.entity.FlowerSightingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface FlowerSightingRepository extends JpaRepository<FlowerSightingEntity, UUID> {

    List<FlowerSightingEntity> findAllByUserUsername(final String username);

    List<FlowerSightingEntity> findAllByUserId(final UUID id);

    List<FlowerSightingEntity> findAllByFlowerName(final String name);

    List<FlowerSightingEntity> findAllByFlowerId(final UUID id);

    FlowerSightingEntity save(final FlowerSightingEntity flowerSightingEntity);

    void deleteById(final UUID id);

    void deleteByUserId(final UUID userId);

    void deleteByIdAndUserId(final UUID id, final UUID userId);
}
