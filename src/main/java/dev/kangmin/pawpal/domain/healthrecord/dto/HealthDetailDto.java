package dev.kangmin.pawpal.domain.healthrecord.dto;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthDetailDto {

    private Long healthId;
    private String dogName;
    private String memberName;
    private Double weight;
    private Double height;
    private String content;
    private LocalDateTime createDate;
    //강아지 과체중,평균, 비만 등 상태
    private String weightState;

    public static HealthDetailDto of(HealthRecord healthRecord) {
        return HealthDetailDto.builder()
                .healthId(healthRecord.getHealthRecordId())
                .content(healthRecord.getContent())
                .dogName(healthRecord.getDog().getName())
                .weight(healthRecord.getWeight())
                .height(healthRecord.getHeight())
                .memberName(healthRecord.getDog().getMember().getName())
                .createDate(healthRecord.getCreateDate())
                .build();
    }

}
