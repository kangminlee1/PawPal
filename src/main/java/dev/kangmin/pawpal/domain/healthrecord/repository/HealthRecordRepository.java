package dev.kangmin.pawpal.domain.healthrecord.repository;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {

    Optional<HealthRecord> findByMemberEmailAndDogIdAndHealthRecordId(String email, Long dogId, Long healthRecordId);
    Optional<HealthRecord> findByMemberEmailAndHealthRecordId(String email, Long healthRecordId);

    List<HealthRecord> findByMemberEmailAndDogId(String email, Long dogId);
}
