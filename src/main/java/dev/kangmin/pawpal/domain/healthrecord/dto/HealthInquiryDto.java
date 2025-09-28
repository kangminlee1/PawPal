package dev.kangmin.pawpal.domain.healthrecord.dto;


import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class HealthInquiryDto {

    private Long healthId;
    private String dogName;
    private String content;
    private Date createDate;

    public static HealthInquiryDto of(HealthRecord healthRecord) {
        return HealthInquiryDto.builder()
                .healthId(healthRecord.getHealthRecordId())
                .content(healthRecord.getContent())
                .dogName(healthRecord.getDog().getName())
                .createDate(healthRecord.getCreateDate())
                .build();
    }
}
