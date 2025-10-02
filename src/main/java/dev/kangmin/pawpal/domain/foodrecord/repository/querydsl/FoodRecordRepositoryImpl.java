package dev.kangmin.pawpal.domain.foodrecord.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodRecordRepositoryImpl implements FoodRecordRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public FoodRecord findByMemberEmailAndDogIdAndFoodRecordId(String email, Long dogId, Long foodRecordId) {
        return null;
    }

    @Override
    public List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogId(String email, Long dogId) {
        return List.of();
    }

    @Override
    public List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogIdOrderByPreference(String email, Long dogId) {
        return List.of();
    }

    @Override
    public List<FoodRecord> findFoodListByDogId(Long dogId) {
        return List.of();
    }
}
