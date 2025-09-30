package dev.kangmin.pawpal.domain.vaccinationrecord.dto;

import dev.kangmin.pawpal.domain.enums.VaccineType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccineInfoDto {

    private Long vaccineId;
    private VaccineType vaccineType;
    private int num;
    private Date vaccinationDate;


}
