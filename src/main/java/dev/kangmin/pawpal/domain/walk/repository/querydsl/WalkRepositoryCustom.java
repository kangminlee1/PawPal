package dev.kangmin.pawpal.domain.walk.repository.querydsl;

import dev.kangmin.pawpal.domain.walk.Walk;
import dev.kangmin.pawpal.domain.walk.dto.WalkStatisticsDto;
import dev.kangmin.pawpal.domain.walk.service.WalkService;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WalkRepositoryCustom {

    Optional<Walk> findByWalkId(Long walkId);

    Optional<Walk> findByWalkIdAndMemberId(Long walkId, Long memberId);

    void deleteByWalkId(Long walkId);

    WalkStatisticsDto findWalksByOwnerAndDogWithinPeriod(Long memberId, Long walkId, LocalDateTime start, LocalDateTime end);

}
