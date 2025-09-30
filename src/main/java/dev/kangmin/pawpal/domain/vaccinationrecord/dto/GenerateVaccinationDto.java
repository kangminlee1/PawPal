package dev.kangmin.pawpal.domain.vaccinationrecord.dto;

import dev.kangmin.pawpal.domain.enums.VaccineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateVaccinationDto {

    private Long dogId;
    private String name;
    private VaccineType vaccineType;
    private int num;

}
