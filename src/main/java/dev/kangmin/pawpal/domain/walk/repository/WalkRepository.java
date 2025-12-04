package dev.kangmin.pawpal.domain.walk.repository;

import dev.kangmin.pawpal.domain.walk.Walk;
import dev.kangmin.pawpal.domain.walk.repository.querydsl.WalkRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long>, WalkRepositoryCustom {

}
