package dev.kangmin.pawpal.domain.healthrecord.dto;


import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
public class HealthInquiryDto {

    private Long dogId;
    private Long healthId;
    private String dogName;
    private String content;
    private LocalDateTime createDate;

    public static HealthInquiryDto of(HealthRecord healthRecord) {
        return HealthInquiryDto.builder()
                .dogId(healthRecord.getDog().getDogId())
                .healthId(healthRecord.getHealthRecordId())
                .content(healthRecord.getContent())
                .dogName(healthRecord.getDog().getName())
                .createDate(healthRecord.getCreateDate())
                .build();
    }
}
