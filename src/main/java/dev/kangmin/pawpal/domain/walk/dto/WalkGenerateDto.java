package dev.kangmin.pawpal.domain.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkGenerateDto {

    private Long dogId;
    private Long duration;
    private Double distance;
    private String startPoint;
    private String endPoint;
}
