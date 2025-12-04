package dev.kangmin.pawpal.domain.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkInfoDto {

    private Long walkId;
    private Long dogId;
}
