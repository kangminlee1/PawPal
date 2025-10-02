package dev.kangmin.pawpal.domain.healthrecord.repository.querydsl;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepositoryCustom {
    Optional<HealthRecord> findByMemberEmailAndDogIdAndHealthRecordId(String email, Long dogId, Long healthRecordId);

    Optional<HealthRecord> findByMemberEmailAndHealthRecordId(String email, Long healthRecordId);

    List<HealthRecord> findByMemberEmail(String email);

    List<HealthRecord> findByMemberEmailOrderByCreateDate(String email, boolean sortBy);

    List<HealthRecord> findByMemberEmailAndDogName(String email, String dogName);
}
