package dev.kangmin.pawpal.domain.walk.repository.querydsl;

import dev.kangmin.pawpal.domain.walk.Walk;

import java.util.Optional;

public interface WalkRepositoryCustom {

    Optional<Walk> findByWalkId(Long walkId);

    void deleteByWalkId(Long walkId);

}
