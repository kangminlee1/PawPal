package dev.kangmin.pawpal.domain.foodrecord.repository;

import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRecordRepository extends JpaRepository<FoodRecord, Long> {
}
