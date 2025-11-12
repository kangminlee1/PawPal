package dev.kangmin.pawpal.domain.healthrecord;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInfoDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long healthRecordId;

    private double weight;
    private double height;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime updateDate;

    private String weightState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;

    public void modifiedHealthInfo(HealthInfoDto healthInfoDto) {
        this.content = healthInfoDto.getContent();
        this.weight = healthInfoDto.getWeight();
        this.height = healthInfoDto.getHeight();
    }


}
