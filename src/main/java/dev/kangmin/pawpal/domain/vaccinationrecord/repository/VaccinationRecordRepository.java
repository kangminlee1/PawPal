package dev.kangmin.pawpal.domain.vaccinationrecord.repository;

import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
}
