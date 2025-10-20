package dev.kangmin.pawpal.domain.healthrecord.dto;


import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
