package dev.kangmin.pawpal.domain.healthrecord.repository;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
}
