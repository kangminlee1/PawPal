package dev.kangmin.pawpal.domain.foodrecord.dto.stats;

import dev.kangmin.pawpal.domain.enums.FoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodTypeNameDto{
    private String foodName;
    private FoodType foodType;
}
