package dev.kangmin.pawpal.domain.DogBreed.repository.querydsl;

import dev.kangmin.pawpal.domain.DogBreed.dto.DogBreedInfoDto;

import java.util.List;

public interface DogBreedRepositoryCustom {
    List<DogBreedInfoDto> findDogBreedInfo();

}
