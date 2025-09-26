package dev.kangmin.pawpal.domain.dog;

import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Dog {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long dogId;

    private String breed;
    private String name;
    private boolean isNeutralizing;
    private int age;
    private String image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "dog")
    private List<HealthRecord> healthRecordList;

    @OneToMany(mappedBy = "dog")
    private List<VaccinationRecord> vaccinationRecordList;

    @OneToMany(mappedBy = "dog")
    private List<FoodRecord> foodRecordList;
}
