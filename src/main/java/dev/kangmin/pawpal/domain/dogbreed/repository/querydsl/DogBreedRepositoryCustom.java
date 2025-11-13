package dev.kangmin.pawpal.domain.dogbreed.repository.querydsl;

import dev.kangmin.pawpal.domain.dogbreed.dto.DogBreedInfoDto;

import java.util.List;

public interface DogBreedRepositoryCustom {
    List<DogBreedInfoDto> findDogBreedInfo();

}
