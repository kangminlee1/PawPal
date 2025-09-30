package dev.kangmin.pawpal.domain.vaccinationrecord.dto;

import dev.kangmin.pawpal.domain.enums.VaccineType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyVaccineDto {

    private Long vaccinationRecordId;
    private Long dogId;
    private VaccineType vaccineType;
    private int num;

}
