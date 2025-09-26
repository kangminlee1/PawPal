package dev.kangmin.pawpal.domain.vaccinationrecord;

import dev.kangmin.pawpal.domain.dog.Dog;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VaccinationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaccinationRecordId;

    private String name;
    private Date vaccinationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;
}
