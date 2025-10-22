package dev.kangmin.pawpal.domain.foodrecord.repository.querydsl;

import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodDetailDto;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodInfoDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FoodRecordRepositoryCustom {
    Optional<FoodRecord> findByMemberAndFoodRecordId(Member member, Long foodRecordId);

//    FoodRecord findByMemberEmailAndDogIdAndFoodRecordId(String email, Long dogId, Long foodRecordId);

    Page<FoodInfoDto> findFoodInfoDtoListByMember(Member member, Pageable pageable);

    Page<FoodInfoDto> findFoodInfoDtoListByMemberAndDogId(Member member, Long dogId, Pageable pageable);

    Page<FoodInfoDto> findFoodInfoDtoListByMemberAndDogIdOrderByPreference(Member member, Long dogId, Pageable pageable);

    List<FoodRecord> findFoodListByDogId(Long dogId);

    FoodDetailDto findDetailByMemberAndDogIdAndFoodRecordId(Member member, Long dogId, Long foodRecordId);

}
