package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.dog.Dog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DogRepositoryImpl implements DogRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Dog> findByMemberEmailAndSortBy(String email, boolean sortBy) {
        return List.of();
    }

    @Override
    public List<Dog> findByMemberEmail(String email) {
        return List.of();
    }

    @Override
    public List<Dog> findByMemberEmailAndBreed(String email, String breed) {
        return List.of();
    }
}
