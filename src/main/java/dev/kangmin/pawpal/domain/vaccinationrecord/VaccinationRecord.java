package dev.kangmin.pawpal.domain.vaccinationrecord;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.enums.VaccineType;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.ModifyVaccineDto;
import dev.kangmin.pawpal.domain.vaccinationrecord.dto.VaccineDetailDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

import static jakarta.persistence.EnumType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaccinationRecordId;

    @Enumerated(STRING)
    private VaccineType vaccineType;
    private int doseNum;
    @CreatedDate
    private Date vaccinationDate;
    @LastModifiedDate
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;

    public void modifyVaccine(ModifyVaccineDto modifyVaccineDto) {
        this.vaccineType = modifyVaccineDto.getVaccineType();
        this.doseNum = modifyVaccineDto.getNum();
    }


}
