package dev.kangmin.pawpal.domain.healthrecord.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthDetailDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInfoDto;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import dev.kangmin.pawpal.domain.healthrecord.repository.HealthRecordRepository;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 건강 검진 결과 등록
    @Transactional
    public void generateMyDogHealthRecord(String email, HealthInfoDto healthInfoDto) {

        //사용자의 강아지가 맞는 지 확인
        Dog myDog = dogService.findDogByMemberEmailAndDogId(email, healthInfoDto.getDogId());
        HealthRecord healthRecord = HealthRecord.builder()
                .dog(myDog)
                .content(healthInfoDto.getContent())
                .height(healthInfoDto.getHeight())
                .weight(healthInfoDto.getWeight())
                .build();

        healthRecordRepository.save(healthRecord);
    }
    //사용자가 바로 건강 검진 탭으로 넘어가는 형식으로 결정

    //내 강아지의 건강 검진 정보 찾기
    public HealthRecord findMyDogHealthRecordByEmailAndDogIdAndHealthRecordId(String email, Long dogId, Long healthId) {
        return healthRecordRepository.findByMemberEmailAndDogIdAndHealthRecordId(email, dogId, healthId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, HEALTH_INFO_IS_NOT_EXISTS));
    }

    public HealthRecord findMyDogHealthRecordByEmailAndHealthRecordId(String email, Long healthRecordId) {
        return healthRecordRepository.findByMemberEmailAndHealthRecordId(email, healthRecordId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, HEALTH_INFO_IS_NOT_EXISTS));
    }

    //건강 검진 결과 수정
    @Transactional
    public void modifiedMyDogHealthRecord(String email, HealthInfoDto healthInfoDto) {
        HealthRecord newHealthRecord = findMyDogHealthRecordByEmailAndDogIdAndHealthRecordId(email, healthInfoDto.getDogId(), healthInfoDto.getHealthInfoId());

        newHealthRecord.modifiedHealthInfo(healthInfoDto);
    }

    //건강 검진 목록 조회
    public List<HealthInquiryDto> getMyDogHealthInquiry(String email) {
        List<HealthRecord> findHealthRecordList = healthRecordRepository.findByMemberEmail(email);

        return findHealthRecordList.stream()
                .map(HealthInquiryDto::of)
                .toList();
    }

    // 건강검진 날짜별 조회
    public List<HealthInquiryDto> getMyDogHealthInquiryOrderByCreateDate(String email, boolean sortBy){
        List<HealthRecord> findHealthRecordList = healthRecordRepository.findByMemberEmailOrderByCreateDate(email, sortBy);
        return findHealthRecordList.stream()
                .map(HealthInquiryDto::of)
                .toList();
    }

    //건강 검진 강아지(이름으로 찾자) 별 조회
    public List<HealthInquiryDto> getMyDogHealthInquiryByName(String email, String dogName) {
        List<HealthRecord> findHealthRecordList = healthRecordRepository.findByMemberEmailAndDogName(email, dogName);
        return findHealthRecordList.stream()
                .map(HealthInquiryDto::of)
                .toList();
    }

    //건강 검진 세부 조회
    public HealthDetailDto getMyDotHealthDetailRecord(String email, Long healthRecordId) {

        return HealthDetailDto.of(findMyDogHealthRecordByEmailAndHealthRecordId(email, healthRecordId));
    }


    //건강 검진 이메일 알림?? 아니면 이메일 전용으로 따로 뺄까?
}
