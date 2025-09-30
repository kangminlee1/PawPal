package dev.kangmin.pawpal.domain.foodrecord.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopWorstFoodDto {

    private List<FoodTypeNameDto> worstFoodTypeNameDtoList;
    private List<FoodTypeNameDto> bestFoodTypeNameDtoList;
}
