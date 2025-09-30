package dev.kangmin.pawpal.domain.foodrecord.dto.stats;

import lombok.Builder;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodRatioDto {

    private double foodRatio;
    private double treatRatio;
}
