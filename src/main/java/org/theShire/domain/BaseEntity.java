package org.theShire.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static org.theShire.foundation.DomainAssertion.isAfterTime;


public class BaseEntity {
    // The Unique Identifier of the entity
    private UUID entityId;
    // The time the entity was created at
    private Instant createdAt;
    // The last time the entity was updated
    private Instant updatedAt;

    public BaseEntity() {
        entityId = UUID.randomUUID();
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    public BaseEntity(UUID uuid) {
        entityId = uuid;
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    public BaseEntity(UUID uuid, Instant createdAt, Instant updatedAt) {
        this.entityId = uuid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        isAfterTime(updatedAt, createdAt, "updatedAt", RuntimeException.class);
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entityId);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.
                append("entityId: ").append(entityId).append(System.lineSeparator()).
                append("createdAt").append(createdAt).append(System.lineSeparator()).
                append("updatedAt").append(updatedAt).append(System.lineSeparator());
        return stringBuilder.toString();
    }

    public String toCSVString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.
                append(entityId).append(";").
                append(createdAt).append(";").
                append(updatedAt).append(";");
        return stringBuilder.toString();
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }
}