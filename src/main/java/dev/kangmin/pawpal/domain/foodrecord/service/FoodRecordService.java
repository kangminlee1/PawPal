package dev.kangmin.pawpal.domain.foodrecord.service;


import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.foodrecord.dto.*;
import dev.kangmin.pawpal.domain.foodrecord.dto.stats.FoodRatioDto;
import dev.kangmin.pawpal.domain.foodrecord.dto.stats.FoodTypeNameDto;
import dev.kangmin.pawpal.domain.foodrecord.dto.stats.TopWorstFoodDto;
import dev.kangmin.pawpal.domain.foodrecord.repository.FoodRecordRepository;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.FOOD_INFO_IS_NOT_EXISTS;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class FoodRecordService {

    private final FoodRecordRepository foodRecordRepository;
    private final MemberService memberService;
    private final DogService dogService;

    /**
     * 강아지 사료/간식 정보 등록
     * @param member
     * @param foodDto
     */
    @Transactional
    public void generateFoodRecord(Member member, GenerateFoodDto foodDto) {
        //해당 하는 강아지
        Dog dog = dogService.findDogByMemberIdAndDogId(member.getMemberId(), foodDto.getDogId());

        FoodRecord foodRecord = FoodRecord.builder()
                .type(foodDto.getType())
                .dog(dog)
                .amount(foodDto.getAmount())
                .name(foodDto.getName())
                .preference(foodDto.getPreference())
                .build();

        foodRecordRepository.save(foodRecord);
    }

    /**
     * 강아지 사료/간식 정보 수정
     * @param modifyFoodDto
     */
    @Transactional
    public void modifyFoodRecord(Member member, ModifyFoodDto modifyFoodDto) {
        FoodRecord foodRecord = findFoodRecordByMemberAndFoodRecordId(member, modifyFoodDto.getFoodRecordId());
        foodRecord.modifyFoodRecord(modifyFoodDto);
    }


    /**
     * 강아지 사료/간식 정보 찾기
     * @param foodRecordId
     * @return
     */
    public FoodRecord findFoodRecordByMemberAndFoodRecordId(Member member, Long foodRecordId) {
        return foodRecordRepository.findByMemberAndFoodRecordId(member, foodRecordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, FOOD_INFO_IS_NOT_EXISTS));
    }

    //-------------사료/간식 정보 조회

    //강아지 이름, ID 값은 메인 페이지 첫 진입 시 내려주고,
    // 강아지 정보 수정 시 다시 프론트가 해당 API를 재호출해서 다시 받는 구조라는 설정
    /**
     * 나의 모든 강아지의 사료/간식 전체 조회
     * @param member
     * @param page
     * @param size
     * @return
     */
    public Page<FoodInfoDto> getMyAllDogsFoodInfoList(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foodRecordRepository.findFoodInfoDtoListByMember(member, pageable);
    }

    /**
     * 사용자의 특정 강아지 사료/간식 정보 전체 조회
     * @param member
     * @param dogId
     * @return
     */
    public Page<FoodInfoDto> getMyDogFoodInfoList(Member member, Long dogId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return foodRecordRepository.findFoodInfoDtoListByMemberAndDogId(member, dogId, pageable);
//        return foodRecordRepository.findFoodInfoDtoListByMemberAndDogName(member, dogName).stream()
//                .map(FoodInfoDto::of)
//                .toList();
    }

    /**
     * 사용자의 특정 강아지 사료/간식 정보
     * 선호도 순 조회
     * @param member
     * @param dogId
     * @return
     */
    public Page<FoodInfoDto> getMyDogFoodInfoListOrderByPreference(Member member, Long dogId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return foodRecordRepository.findFoodInfoDtoListByMemberAndDogIdOrderByPreference(member, dogId, pageable);

//        return foodRecordRepository.findFoodInfoDtoListByMemberAndDogNameOrderByPreference(email, dogId).stream()
//                .map(FoodInfoDto::of)
//                .toList();
    }

    /**
     * 간식 세부 정보 조회
     * @param member
     * @param dogId
     * @param foodRecordId
     * @param page
     * @param size
     * @return
     */
    public FoodDetailDto getMyDodFoodDetail(Member member, Long dogId, Long foodRecordId, int page, int size) {
        return foodRecordRepository.findDetailByMemberAndDogIdAndFoodRecordId(member, dogId, foodRecordId);
//        return Optional.ofNullable(foodRecordRepository.findByMemberEmailAndDogIdAndFoodRecordId(email, dogId, foodRecordId))
//                .map(FoodDetailDto::of)
//                .orElseThrow(() -> new CustomException(BAD_REQUEST, ErrorCode.FOOD_INFO_IS_NOT_EXISTS));
    }



    //// 통계 쪽
    //강아지가 1달간 섭취한 사료/간식량 평균(총 섭취량 / 1달)
    //아직 좀 더 생각
    //강아지 음식 선호도 top 5 / worst 5
    public TopWorstFoodDto getTopWorstFoodList(Long dogId) {
        List<FoodRecord> foodRecordList = foodRecordRepository.findFoodListByDogId(dogId);

        //가장 선호하는 top 5 사료 및 간식
        List<FoodTypeNameDto> favorFoodList = foodRecordList.stream()
                .sorted(Comparator.comparing(FoodRecord::getPreference).reversed())
                .filter(food -> food.getPreference() >= 7)
                .limit(5)
                .map(food -> FoodTypeNameDto.builder()
                        .foodName(food.getName())
                        .foodType(food.getType())
                        .build())
                .toList();

        //가장 선호도가 낮은 top 5 사료 및 간식
        List<FoodTypeNameDto> worstFoodList = foodRecordList.stream()
                .sorted(Comparator.comparing(FoodRecord::getPreference))
                .filter(food -> food.getPreference() <= 3)
                .limit(5)
                .map(food -> FoodTypeNameDto.builder()
                        .foodName(food.getName())
                        .foodType(food.getType())
                        .build())
                .toList();

        return TopWorstFoodDto.builder()
                .bestFoodTypeNameDtoList(favorFoodList)
                .worstFoodTypeNameDtoList(worstFoodList)
                .build();
    }


    //간식 / 사료 비율 -> 간식/사료 세부 정보 보여줄때 같이 넘겨줄 정보
    public FoodRatioDto getMyDogFoodRatio(Long dogId) {
        List<FoodRecord> foodRecordList = foodRecordRepository.findFoodListByDogId(dogId);

        long treatCount = foodRecordList.stream()
                .filter(n -> "TREAT".equals(n.getType().name()))
                .count();

        long foodCount = foodRecordList.stream()
                .filter(n -> "FOOD".equals(n.getType().name()))
                .count();
        long total = treatCount + foodCount;

        double foodRatio = 0;
        double treatRatio = 0;
        if (total>0) {
            foodRatio = (double) foodCount / total * 100;
            treatRatio = (double) treatCount / total * 100;
        }
            return FoodRatioDto.builder()
                    .foodRatio(foodRatio)
                    .treatRatio(treatRatio)
                    .build();

    }

}
