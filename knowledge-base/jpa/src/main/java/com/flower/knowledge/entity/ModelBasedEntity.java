package com.flower.knowledge.entity;

/**
 * A model based entity allows users to convert between domain models and entities.
 * <p>
 * Usually, implementing entities also provide a static builder method to instantiates them from domain models,
 * though this is something which can not be enforced by an interface.
 *
 * @param <M> the type of the domain model
 */
public interface ModelBasedEntity<M> {

    /**
     * Create the associated {@link M domain model} for this entity.
     *
     * @return the domain model instance.
     */
    M toDomainModel();
}
