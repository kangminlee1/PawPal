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

    //강아지의 사료/간식 정보 등록
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

    //수정
    @Transactional
    public void modifyFoodRecord(ModifyFoodDto modifyFoodDto) {
        FoodRecord foodRecord = findFoodRecordByFoodRecordId(modifyFoodDto.getFoodRecordId());
        foodRecord.modifyFoodRecord(modifyFoodDto);
    }


    ///////////////////////////////
    //강아지 사료/간식 정보 찾기 feat ID
    public FoodRecord findFoodRecordByFoodRecordId(Long foodRecordId) {
        return foodRecordRepository.findByFoodRecordId(foodRecordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, FOOD_INFO_IS_NOT_EXISTS));
    }

    //-------------사료/간식 정보 조회
    //해당 강아지의 사료/간식 전체 정보 조회
    public List<FoodInfoDto> getMyDogFoodInfoList(String email, Long dogId) {
        return foodRecordRepository.findFoodInfoDtoListByMemberEmailAndDogId(email, dogId).stream()
                .map(FoodInfoDto::of)
                .toList();
    }

    //선호도 순
    public List<FoodInfoDto> getMyDogFoodInfoListOrderByPreference(String email, Long dogId) {
        return foodRecordRepository.findFoodInfoDtoListByMemberEmailAndDogIdOrderByPreference(email, dogId).stream()
                .map(FoodInfoDto::of)
                .toList();
    }


    //사료 간식 세부 정보
    public FoodDetailDto getMyDodFoodDetail(String email, Long dogId, Long foodRecordId) {
        return Optional.ofNullable(foodRecordRepository.findByMemberEmailAndDogIdAndFoodRecordId(email, dogId, foodRecordId))
                .map(FoodDetailDto::of)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, ErrorCode.FOOD_INFO_IS_NOT_EXISTS));
    }



    //// 통계 쪽
    //강아지가 1달간 섭취한 사료/간식량 평균(총 섭취량 / 1달)
    //아직 좀 더 생각
    //

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
