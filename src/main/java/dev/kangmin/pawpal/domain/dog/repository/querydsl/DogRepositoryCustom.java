package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import dev.kangmin.pawpal.domain.dog.Dog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepositoryCustom {

    List<Dog> findByMemberEmailAndSortBy(String email, boolean sortBy);

    List<Dog> findByMemberEmail(String email);

    List<Dog>findByMemberEmailAndBreed(String email, String breed);

}
