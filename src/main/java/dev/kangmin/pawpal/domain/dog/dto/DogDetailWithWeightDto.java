package dev.kangmin.pawpal.domain.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogDetailWithWeightDto {
    DogDetailDto dogDetailDto;
    List<DogWeightDto> dogWeightDtoList;
}
