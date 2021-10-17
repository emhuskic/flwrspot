package com.flower.knowledge.repository;

import com.flower.knowledge.entity.FlowerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface FlowerRepository extends PagingAndSortingRepository<FlowerEntity, UUID> {

    Optional<FlowerEntity> findByName(final String name);

    Page<FlowerEntity> findAll(final Pageable pageable);
}