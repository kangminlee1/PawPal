package dev.kangmin.pawpal.domain.dogbreed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogBreedInfoDto {

    private Long dogBreedId;
    private String breedName;

}
