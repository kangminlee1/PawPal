package dev.kangmin.pawpal.domain.healthrecord.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthDetailDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInfoDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import dev.kangmin.pawpal.domain.healthrecord.repository.HealthRecordRepository;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthRecordService {

    private final MemberService memberService;
    private final HealthRecordRepository healthRecordRepository;
    private final DogService dogService;

    /**
     * 강아지 건강 검진 결과 등록
     *
     * @param member
     * @param healthInfoDto
     */
    @Transactional
    public void generateMyDogHealthRecord(Member member, HealthInfoDto healthInfoDto) {

        //사용자의 강아지가 맞는 지 확인
        Dog myDog = dogService.findDogByMemberIdAndDogId(member.getMemberId(), healthInfoDto.getDogId());

        //여기에 강아지 나이대 별로 저장
        //1세 이하 1달 기준
        //2~6세 반년
        //7세 이상 3개월

        int age = myDog.getAge();
        LocalDateTime now = LocalDateTime.now();
        if (age <= 1) {
            myDog.modifiedDate(now, now.plusMonths(1));
        } else if (age <= 6) {
            myDog.modifiedDate(now, now.plusMonths(6));
        } else {
            myDog.modifiedDate(now, now.plusMonths(3));
        }

        HealthRecord healthRecord = HealthRecord.builder()
                .dog(myDog)
                .content(healthInfoDto.getContent())
                .height(healthInfoDto.getHeight())
                .weight(healthInfoDto.getWeight())
                .build();

        healthRecordRepository.save(healthRecord);
    }
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

    /**
     * 나의 강아지 건강 검진 세부 기록
     *
     * @param member
     * @param healthRecordId
     * @return
     */
    public HealthDetailDto getMyDotHealthDetailRecord(Member member, Long healthRecordId) {
        //querydsl로 N+1 해결(fetchJoin
        return HealthDetailDto.of(findMyDogHealthRecordByMemberAndHealthRecordId(member, healthRecordId));
    }
}
