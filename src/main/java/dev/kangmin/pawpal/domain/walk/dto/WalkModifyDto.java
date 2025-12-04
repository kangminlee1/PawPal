package dev.kangmin.pawpal.domain.walk.dto;

import lombok.Getter;

@Getter
public class WalkModifyDto {

    private Long walkId;
    private Long duration;
    private Double distance;
    private String startPoint;
    private String endPoint;

}
