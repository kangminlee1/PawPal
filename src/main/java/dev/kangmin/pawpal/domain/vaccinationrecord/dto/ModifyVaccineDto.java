package dev.kangmin.pawpal.domain.vaccinationrecord.dto;

import dev.kangmin.pawpal.domain.enums.VaccineType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyVaccineDto {

    @NotBlank
    private Long vaccinationRecordId;
    @NotBlank
    private Long dogId;
    private VaccineType vaccineType;
    private int num;

}
