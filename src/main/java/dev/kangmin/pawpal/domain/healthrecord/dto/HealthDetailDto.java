package dev.kangmin.pawpal.domain.healthrecord.dto;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
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
    private Date createDate;

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
