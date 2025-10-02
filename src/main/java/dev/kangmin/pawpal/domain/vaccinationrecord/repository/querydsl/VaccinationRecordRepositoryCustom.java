package dev.kangmin.pawpal.domain.vaccinationrecord.repository.querydsl;


import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;

import java.util.List;
import java.util.Optional;

public interface VaccinationRecordRepositoryCustom {
    Optional<VaccinationRecord> findByVaccinationRecordIdAndMemberEmail(Long vaccinationRecordId, String email);

    List<VaccineInfoDto> findVaccinationRecordListByMemberEmailAndDogId(String email, Long dogId);

    VaccinationRecord findByMemberEmailAndDogIdAndVaccinationRecordId(String email, Long dogId, Long vaccinationRecordId);
}
