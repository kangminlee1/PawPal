package dev.kangmin.pawpal.domain.vaccinationrecord.dto;

import dev.kangmin.pawpal.domain.enums.VaccineType;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccineDetailDto {

    private Long dogId;
    private Long vaccinationRecordId;
    private VaccineType vaccineType;
    private int doseNum;
    private LocalDateTime vaccinationDate;

    //entity -> dto
    public static VaccineDetailDto of(VaccinationRecord vaccinationRecord) {
        return VaccineDetailDto.builder()
                .dogId(vaccinationRecord.getDog().getDogId())
                .vaccinationRecordId(vaccinationRecord.getVaccinationRecordId())
                .vaccineType(vaccinationRecord.getVaccineType())
                .vaccinationDate(vaccinationRecord.getVaccinationDate())
                .doseNum(vaccinationRecord.getDoseNum())
                .build();
    }
}
