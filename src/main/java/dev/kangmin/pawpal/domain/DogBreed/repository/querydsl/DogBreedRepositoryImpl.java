package dev.kangmin.pawpal.domain.DogBreed.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.DogBreed.DogBreed;
import dev.kangmin.pawpal.domain.DogBreed.dto.DogBreedInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.kangmin.pawpal.domain.DogBreed.QDogBreed.dogBreed;

@Repository
@RequiredArgsConstructor
public class DogBreedRepositoryImpl implements DogBreedRepositoryCustom {

    private JPAQueryFactory queryFactory;

    @Override
    public List<DogBreedInfoDto> findDogBreedInfo() {
        return queryFactory
                .select(
                        Projections.constructor(
                                DogBreedInfoDto.class,
                                dogBreed.dogBreedId,
                                dogBreed.breed
                        )
                )
                .from(dogBreed)
                .fetch();
    }
}
