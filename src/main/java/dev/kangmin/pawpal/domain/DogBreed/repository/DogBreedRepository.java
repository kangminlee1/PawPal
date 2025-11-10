package dev.kangmin.pawpal.domain.DogBreed.repository;

import dev.kangmin.pawpal.domain.DogBreed.DogBreed;
import dev.kangmin.pawpal.domain.DogBreed.repository.querydsl.DogBreedRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogBreedRepository extends JpaRepository<DogBreed, Long>, DogBreedRepositoryCustom {

}
