package dev.kangmin.pawpal.domain.healthrecord.service;

import dev.kangmin.pawpal.domain.dogbreed.DogBreed;
import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.dto.DogWeightDto;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthDetailDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInfoDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import dev.kangmin.pawpal.domain.healthrecord.repository.HealthRecordRepository;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;
    private final DogService dogService;

    //사용자가 바로 건강 검진 탭으로 넘어가는 형식으로 결정
    /**
     * 나의 강아지 건강 검진 정보 찾기
     *
     * @param member
     * @param dogId
     * @param healthId
     * @return
     */
    public HealthRecord findMyDogHealthRecordByMemberAndDogIdAndHealthRecordId(Member member, Long dogId, Long healthId) {
        return healthRecordRepository.findByMemberAndDogIdAndHealthRecordId(member, dogId, healthId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, HEALTH_INFO_IS_NOT_EXISTS));
    }

    /**
     * 나의 강아지 건강 검진 기록 찾기
     *
     * @param member
     * @param healthRecordId
     * @return
     */
    public HealthRecord findMyDogHealthRecordByMemberAndHealthRecordId(Member member, Long healthRecordId) {
        return healthRecordRepository.findByMemberAndHealthRecordId(member, healthRecordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, HEALTH_INFO_IS_NOT_EXISTS));
    }

    /**
     * id값으로 강아지의 건강 검진 기록 찾기
     * @param healthId
     * @return
     */
    public HealthRecord findByHealthRecordId(Long healthId) {
        return healthRecordRepository.findByHealthRecordId(healthId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, HEALTH_INFO_IS_NOT_EXISTS));
    }

    /**
     * 강아지 건강 검진 정보 수정
     *
     * @param member
     * @param healthInfoDto
     */
    @Transactional
    public void modifiedMyDogHealthRecord(Member member, HealthInfoDto healthInfoDto) {
        HealthRecord newHealthRecord = findMyDogHealthRecordByMemberAndDogIdAndHealthRecordId(member, healthInfoDto.getDogId(), healthInfoDto.getHealthInfoId());
        newHealthRecord.modifiedHealthInfo(healthInfoDto);
    }

//    /**
//     * 사용자의 모든 강아지 건강 검진 목록 조회
//     *
//     * @param member
//     * @return
//     */
//    public Page<HealthInquiryDto> getMyDogHealthInquiry(Member member, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return healthRecordRepository.findByMember(member, pageable);
//
////        return findHealthRecordList.stream()
////                .map(HealthInquiryDto::of)
////                .toList();
//    }

    /**
     * 사용자의 모든 강아지 건강 검진 목록 조회
     * 기본 최신순
     * 최신, 오래된 순 정렬
     *
     * @param member
     * @param sortBy
     * @param page
     * @param size
     * @return
     */
    public Page<HealthInquiryDto> getMyDogHealthInquiryOrderByCreateDate(Member member, Boolean sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return healthRecordRepository.findByMemberOrderByCreateDate(member, sortBy, pageable);
//        return findHealthRecordList.stream()
//                .map(HealthInquiryDto::of)
//                .toList();
    }

    /**
     * 강아지 이름으로 찾기
     *
     * @param member
     * @param dogName
     * @param page
     * @param size
     * @return
     */
    public Page<HealthInquiryDto> getMyDogHealthInquiryByName(Member member, String dogName, Boolean sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return healthRecordRepository.findByMemberAndDogName(member, dogName, sortBy, pageable);
//        return findHealthRecordList.stream()
//                .map(HealthInquiryDto::of)
//                .toList();
    }

    //강아지 세부 기록 볼때 비만 등 상태  +  현재 강아지가 평균에 비해 %로 얼마만큼 차이가 나는가
    /**
     * 나의 강아지 건강 검진 세부 기록
     *
     * @param member
     * @param healthRecordId
     * @return
     */
    public HealthDetailDto getMyDotHealthDetailRecord(Member member, Long healthRecordId) {
        HealthRecord healthRecord = findMyDogHealthRecordByMemberAndHealthRecordId(member, healthRecordId);
        DogBreed dogBreed = healthRecord.getDog().getDogBreed();

        HealthDetailDto healthDetailDto = HealthDetailDto.of(healthRecord);
        healthDetailDto.setWeightDeviationPercent(
                calcWeight(healthDetailDto.getWeight(), dogBreed.getMinWeight(), dogBreed.getMaxWeight())
        );

        return healthDetailDto;
    }

    //강아지 건강 검진 통계 -> 건강검진 통계 값은 가장 마지막 데이터 가장 첫번째 데이터랑 비교해서 얼마만큼 찌고 빠졌는지 %로 데이터 추가
    //기본적으로는 강아지 세부 정보 페이지에서 주는 정보
    //특정 조건을 변경했을 때 호출할 통계 자료들
    /**
     * 강아지 몸무게 통계 -> 기본
     * 최근 6건
     *
     * @param member
     * @param dogId
     * @return
     */
    public List<DogWeightDto> getDogWeightChangeStatistics(Member member, Long dogId) {
        List<DogWeightDto> dogWeightDtoList = healthRecordRepository.findWeightChangeStaticsByMemberIdAndDogId(member.getMemberId(), dogId);

        Dog dog = dogService.findDogByDogId(dogId);

        double minWeight = dog.getDogBreed().getMinWeight();
        double maxWeight = dog.getDogBreed().getMaxWeight();

        for (DogWeightDto dogWeightDto : dogWeightDtoList) {
            dogWeightDto.setWeightDeviationPercent(
                    calcWeight(dogWeightDto.getWeight(), minWeight, maxWeight)
            );
        }
        return dogWeightDtoList;
    }

    /**
     * 강아지 몸무게 통계 -> 특정 개월 수 기준
     * 현재부터 3, 6, 12개월 전까지 탐색 가능
     *
     * @param member
     * @param dogId
     * @param month
     * @return
     */
    //강아지 몸무게 변화 추세 % 단위 추가?
    public List<DogWeightDto> getDogWeightChangeStatisticsByLastMonth(Member member, Long dogId, int month) {
        List<DogWeightDto> dogWeightDtoList = healthRecordRepository.findWeightChangeStaticsByMemberIdAndDogIdAndMonth(member.getMemberId(), dogId, month);

        Dog dog = dogService.findDogByDogId(dogId);

        double minWeight = dog.getDogBreed().getMinWeight();
        double maxWeight = dog.getDogBreed().getMaxWeight();

        for (DogWeightDto dogWeightDto : dogWeightDtoList) {
            dogWeightDto.setWeightDeviationPercent(
                    calcWeight(dogWeightDto.getWeight(), minWeight, maxWeight)
            );
        }

        return dogWeightDtoList;
    }

    //현재 몸무게 상태 % 계산
    public Double calcWeight(double dogWeight, double minWeight, double maxWeight) {
        if (dogWeight < minWeight) {
            return (dogWeight - minWeight) / minWeight * 100;
        } else if (dogWeight <= maxWeight) {
            return 0.0;
        } else if (dogWeight <= maxWeight * 1.2) {
            return (dogWeight - maxWeight) / maxWeight * 100;
        } else {
            return (dogWeight - maxWeight) / maxWeight * 100;
        }
    }
}
