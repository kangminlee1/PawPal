package dev.kangmin.pawpal.domain.walk.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkStatisticsDto {

    private int walkCount;
    private Double totalDistance;
    private Double averageDuration;
    private Double achievementRate;

    private Map<String, Long> timeZonePattern; // 시간대 패턴

    private LocalDate startDate;
    private LocalDate endDate;

}
