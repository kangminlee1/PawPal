package dev.kangmin.pawpal.domain.healthrecord.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInfoDto;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class HealthRecordManageService {

    private final DogService dogService;
    private final HealthRecordRedisService healthRecordRedisService;

    /**
     * 건강 검진 결과 생성
     * @param healthInfoDto
     * @param dog
     * @return
     */
    public HealthRecord buildHealthRecord(HealthInfoDto healthInfoDto, Dog dog) {

        String weightState;

        double minWeight = dog.getDogBreed().getMinWeight();
        double maxWeight = dog.getDogBreed().getMaxWeight();

        double dogWeight = healthInfoDto.getWeight();
        if (dogWeight < minWeight) {
            weightState = "저체중";
        } else if (dogWeight <= maxWeight) {
            weightState = "정상 체중";
        } else if (dogWeight <= maxWeight * 1.2) {
            weightState = "과체중";
        } else {
            weightState = "비만";
        }

        return HealthRecord.builder()
                .dog(dog)
                .content(healthInfoDto.getContent())
                .height(healthInfoDto.getHeight())
                .weight(healthInfoDto.getWeight())
                .weightState(weightState)
                .build();
    }

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
        LocalDateTime expiredDate;
        if (age <= 1) {
            expiredDate = LocalDateTime.now().plusMonths(1);
        } else if (age <= 6) {
            expiredDate = LocalDateTime.now().plusMonths(6);
        } else {
            expiredDate = LocalDateTime.now().plusMonths(3);
        }

        //건강 정보 생성 후 저장
        HealthRecord createdHealthRecord = buildHealthRecord(healthInfoDto, myDog);
        //redis 키 저장
        healthRecordRedisService.setHealthRecordExpiredTime(member, createdHealthRecord.getHealthRecordId(), expiredDate);
    }
}
