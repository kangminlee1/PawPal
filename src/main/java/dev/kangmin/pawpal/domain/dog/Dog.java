package dev.kangmin.pawpal.domain.dog;

import dev.kangmin.pawpal.domain.dogbreed.DogBreed;
import dev.kangmin.pawpal.domain.dog.dto.UpdateDogDto;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import dev.kangmin.pawpal.domain.walk.Walk;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.EnumType.*;
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

    private String name;
    private boolean isNeutralizing;
    private int age;
    private String image;

    @Enumerated(STRING)
    private ExistStatus existStatus;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogBreedId")
    private DogBreed dogBreed;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthRecord> healthRecordList;

    @OneToMany(mappedBy = "dog",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccinationRecord> vaccinationRecordList;

    @OneToMany(mappedBy = "dog",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodRecord> foodRecordList;

    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Walk> walkList;

    public void modifyInfo(UpdateDogDto dogInfoDto) {
        this.name = dogInfoDto.getName();
        this.isNeutralizing = dogInfoDto.isNeutralizing();
        this.age = dogInfoDto.getAge();
        this.image = dogInfoDto.getImage();
    }

    public void modifyInfo(UpdateDogDto dogInfoDto, DogBreed dogBreed) {
        this.name = dogInfoDto.getName();
        this.isNeutralizing = dogInfoDto.isNeutralizing();
        this.dogBreed = dogBreed;
        this.age = dogInfoDto.getAge();
        this.image = dogInfoDto.getImage();
    }

}
