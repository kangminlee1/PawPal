package dev.kangmin.pawpal.domain.vaccinationrecord.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineDetailDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.domain.vaccinationrecord.QVaccinationRecord.vaccinationRecord;

@Repository
@RequiredArgsConstructor
public class VaccinationRecordRepositoryImpl implements VaccinationRecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //강아지 백신 정보 찾기
    @Override
    public Optional<VaccinationRecord> findVaccinationRecordByMemberIdAndDogIdAndVaccineId(Long memberId, Long dogId, Long vaccinationRecordId) {
        return Optional.ofNullable(
                queryFactory
                        .select(vaccinationRecord)
                        .from(vaccinationRecord)
                        .where(
                                vaccinationRecord.vaccinationRecordId.eq(vaccinationRecordId),
                                vaccinationRecord.dog.member.memberId.eq(memberId)
                        )
                        .fetchOne()
        );
    }

    //내 강아지의 백신 정보 리스트
    @Override
    public List<VaccineInfoDto> findVaccinationRecordListByMemberIdAndDogId(Long memberId, Long dogId) {

        return queryFactory
                .select(Projections.constructor(VaccineInfoDto.class,
                        vaccinationRecord.vaccinationRecordId,
                        vaccinationRecord.vaccineType,
                        vaccinationRecord.doseNum,
                        vaccinationRecord.vaccinationDate
                        )
                )
                .from(vaccinationRecord)
                .where(
                        vaccinationRecord.dog.member.memberId.eq(memberId),
                        vaccinationRecord.dog.dogId.eq(dogId)
                )
                .fetch();
    }
}
