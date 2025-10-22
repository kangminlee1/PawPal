package dev.kangmin.pawpal.domain.dog;

import dev.kangmin.pawpal.domain.dog.dto.DogInfoDto;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.dog.dto.UpdateDogDto;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.vaccinationrecord.VaccinationRecord;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    private ExistStatus existStatus;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    //건강 검진 일
    private LocalDateTime lastHealthCheckDate;
    private LocalDateTime nextHealthCheckDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "dog",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthRecord> healthRecordList;

    @OneToMany(mappedBy = "dog",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccinationRecord> vaccinationRecordList;

    @OneToMany(mappedBy = "dog",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodRecord> foodRecordList;

    public void modifyInfo(UpdateDogDto dogInfoDto) {
        this.name = dogInfoDto.getName();
        this.breed = dogInfoDto.getBreed();
        this.isNeutralizing = dogInfoDto.isNeutralizing();
        this.age = dogInfoDto.getAge();
        this.image = dogInfoDto.getImage();
    }

    public void change(ExistStatus existStatus) {
        this.existStatus = existStatus;
    }

    public void modifiedDate(LocalDateTime lastHealthCheckDate, LocalDateTime nextHealthCheckDate) {
        this.lastHealthCheckDate = lastHealthCheckDate;
        this.nextHealthCheckDate = nextHealthCheckDate;
    }

}
