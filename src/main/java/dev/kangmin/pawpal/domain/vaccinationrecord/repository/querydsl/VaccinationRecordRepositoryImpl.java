package dev.kangmin.pawpal.domain.vaccinationrecord.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VaccinationRecordRepositoryImpl implements VaccinationRecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<VaccinationRecord> findByVaccinationRecordIdAndMemberEmail(Long vaccinationRecordId, String email) {
        return Optional.empty();
    }

    @Override
    public List<VaccineInfoDto> findVaccinationRecordListByMemberEmailAndDogId(String email, Long dogId) {
        return List.of();
    }

    @Override
    public VaccinationRecord findByMemberEmailAndDogIdAndVaccinationRecordId(String email, Long dogId, Long vaccinationRecordId) {
        return null;
    }
}
