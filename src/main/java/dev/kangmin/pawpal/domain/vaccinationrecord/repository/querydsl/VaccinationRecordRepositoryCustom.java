package dev.kangmin.pawpal.domain.vaccinationrecord.repository.querydsl;


import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineDetailDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;

import java.util.List;
import java.util.Optional;

public interface VaccinationRecordRepositoryCustom {
    Optional<VaccinationRecord> findVaccinationRecordByMemberIdAndDogIdAndVaccineId(Long memberId, Long dogId, Long vaccinationRecordId);

    List<VaccineInfoDto> findVaccinationRecordListByMemberIdAndDogId(Long memberId, Long dogId);

//    VaccineDetailDto findByMemberIdAndDogIdAndVaccinationRecordId(Long memberId, Long dogId, Long vaccinationRecordId);
}
