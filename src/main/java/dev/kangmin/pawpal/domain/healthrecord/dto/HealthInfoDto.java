package dev.kangmin.pawpal.domain.healthrecord.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthInfoDto {
    //등록, 수정
    private Long dogId;
    private Long healthInfoId;
    @NotNull(message = "강아지의 몸무게를 입력해주세요.")
    @DecimalMin(value = "0.1", message = "몸무게는 0보다 커야 합니다.")
    @DecimalMax(value = "100", message = "몸무게는 100KG 이하만 가능 합니다.")
    private Double weight;
    @NotNull(message = "강아지의 신장을 입력해주세요.")
    @DecimalMin(value = "0.1", message = "몸길이는 0보다 커야 합니다.")
    @DecimalMax(value = "300", message = "몸길이는 300cm 이하만 가능 합니다.")
    private Double height;
    @NotNull(message = "강아지 건강검진 기록을 입력해주세요.")
    @Size(max = 65535, message = "건강 검진 세부 사항은 65535자 까지만 입력 가능합니다.")
    private String content;
}
