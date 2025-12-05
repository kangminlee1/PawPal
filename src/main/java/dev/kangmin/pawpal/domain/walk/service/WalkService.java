package dev.kangmin.pawpal.domain.walk.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.service.DogService;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.walk.Walk;
import dev.kangmin.pawpal.domain.walk.dto.WalkDetailDto;
import dev.kangmin.pawpal.domain.walk.dto.WalkGenerateDto;
import dev.kangmin.pawpal.domain.walk.dto.WalkModifyDto;
import dev.kangmin.pawpal.domain.walk.dto.WalkStatisticsDto;
import dev.kangmin.pawpal.domain.walk.repository.WalkRepository;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalkService {

    private final WalkRepository walkRepository;
    private final DogService dogService;

    public Walk buildWalk(WalkGenerateDto walkGenerateDto, Dog dog) {
        return Walk.builder()
                .distance(walkGenerateDto.getDistance())
                .duration(walkGenerateDto.getDuration())
                .startPoint(walkGenerateDto.getStartPoint())
                .endPoint(walkGenerateDto.getEndPoint())
                .dog(dog)
                .build();
    }

    //산책 등록 (임시)
    @Transactional
    public WalkDetailDto generateWalk(Member member, WalkGenerateDto walkGenerateDto) {

        Dog myDog = dogService.findDogByDogId(walkGenerateDto.getDogId());

        if (!member.equals(myDog.getMember())) {
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        //추후 지도 API 통과 하면 수정 할 것
        Walk walk = buildWalk(walkGenerateDto, myDog);
        walkRepository.save(walk);

        return WalkDetailDto.of(walk);
    }

    //산책 정보 삭제
    @Transactional
    public void deleteWalk(Member member, Long walkId, Long dogId) {
        Walk walk = findByWalkId(walkId);
        Dog dog = dogService.findDogByDogId(dogId);

        if (!(walk.getDog().equals(dog))) {
            throw new CustomException(FORBIDDEN, WALK_OWNER_MISMATCH);
        }

        if (!(walk.getDog().getMember().equals(member))) {
            throw new CustomException(FORBIDDEN, WALK_DOG_MISMATCH);
        }


        walkRepository.deleteByWalkId(walkId);

    }

    //산책 정보 수정
    public void modifiedWalkInfo(Member member, WalkModifyDto walkModifyDto) {
        Walk walk = findByWalkIdAndMemberId(walkModifyDto.getWalkId(), member.getMemberId());
        walk.modify(walkModifyDto);
    }

    //산책 세부 정보
    public WalkDetailDto getWalKDetails(Member member, Long walkId) {
        Walk walk = findByWalkIdAndMemberId(walkId, member.getMemberId());
        return WalkDetailDto.of(walk);
    }


    //정보 찾기 ------------
    public Walk findByWalkId(Long walkId) {
        return walkRepository.findByWalkId(walkId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, WALK_IS_NOT_EXISTS));
    }

    public Walk findByWalkIdAndMemberId(Long walkId, Long memberId) {
        return walkRepository.findByWalkIdAndMemberId(walkId, memberId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, WALK_OWNER_MISMATCH));
    }

    //통계 -------------
    //이번달 산책 횟수, 총 거리, 평균 산책 시간 , 산책 시간대 패턴, 등 :     // 주간/월간 리포트(차트용 데이터)
    public WalkStatisticsDto getMonthWalkStatisticsCount(Member member, Long dogId) {
        LocalDate now = LocalDate.now();
        LocalDate firstDate = now.withDayOfMonth(1);
        LocalDate lastDate = now.withDayOfMonth(now.lengthOfMonth());

        LocalDateTime start = firstDate.atStartOfDay();
        LocalDateTime end = lastDate.atTime(LocalTime.MAX);

        return walkRepository.findWalksByOwnerAndDogWithinPeriod(member.getMemberId(), dogId, start, end);
    }

    //과거 대비 변화

    //산책 시간대별 패턴(특정 날짜~ 특정날짜 까지)
    //새벽00~0559 아침 06~1059 점심 11~1459 오후 15~1859 저녁 19~2359

}
