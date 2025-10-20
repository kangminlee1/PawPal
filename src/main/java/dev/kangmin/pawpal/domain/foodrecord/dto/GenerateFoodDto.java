package dev.kangmin.pawpal.domain.foodrecord.dto;

import dev.kangmin.pawpal.domain.enums.FoodType;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateFoodDto {

    private Long dogId;
    @NotBlank(message = "사료/간식 이름을 입력해주세요.")
    @Size(max = 50, message = "50자 이내로 입력해주세요")
    private String name;
    @NotBlank(message = "사료나 간식의 타입을 골라주세요.")
    private FoodType type;

    @NotBlank(message = "선호도를 입력해 주세요")
    @Min(0)
    @Max(10)
    private int preference;
    @NotBlank(message = "급여량을 입력해주세요.(g 단위)")
    @Min(1)
    @Max(1000)
    private int amount;
}
