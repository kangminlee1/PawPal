package dev.kangmin.pawpal.domain.foodrecord.repository.querydsl;

import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;

import java.util.List;

public interface FoodRecordRepositoryCustom {
    FoodRecord findByMemberEmailAndDogIdAndFoodRecordId(String email, Long dogId, Long foodRecordId);

    List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogId(String email, Long dogId);

    List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogIdOrderByPreference(String email, Long dogId);

    List<FoodRecord> findFoodListByDogId(Long dogId);

}
