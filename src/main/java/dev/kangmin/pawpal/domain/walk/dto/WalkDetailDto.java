package dev.kangmin.pawpal.domain.walk.dto;

import dev.kangmin.pawpal.domain.walk.Walk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkDetailDto {

    private Long walkId;
    private Long dogId;
    private Long duration;
    private Double distance;
    private String startPoint;
    private String endPoint;

    public static WalkDetailDto of(Walk walk) {
        return WalkDetailDto.builder()
                .walkId(walk.getWalkId())
                .distance(walk.getDistance())
                .duration(walk.getDuration())
                .dogId(walk.getDog().getDogId())
                .startPoint(walk.getStartPoint())
                .endPoint(walk.getEndPoint())
                .build();
    }

}
