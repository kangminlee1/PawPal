package dev.kangmin.pawpal.domain.vaccinationrecord.repository;

import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    Optional<VaccinationRecord> findByVaccinationRecordIdAndMemberEmail(Long vaccinationRecordId, String email);

    List<VaccineInfoDto> findVaccinationRecordListByMemberEmailAndDogId(String email, Long dogId);
}
