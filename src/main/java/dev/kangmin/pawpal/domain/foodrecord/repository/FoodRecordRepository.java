package dev.kangmin.pawpal.domain.foodrecord.repository;

import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodDetailDto;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRecordRepository extends JpaRepository<FoodRecord, Long> {

    Optional<FoodRecord> findByFoodRecordId(Long foodRecordId);

    FoodRecord findByMemberEmailAndDogIdAndFoodRecordId(String email, Long dogId, Long foodRecordId);

    List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogId(String email, Long dogId);

    List<FoodRecord> findFoodInfoDtoListByMemberEmailAndDogIdOrderByPreference(String email, Long dogId);

    List<FoodRecord> findFoodListByDogId(Long dogId);
}
