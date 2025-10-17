package dev.kangmin.pawpal.domain.dog.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.dto.DogDetailDto;
import dev.kangmin.pawpal.domain.dog.dto.DogInfoDto;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.dog.dto.UpdateDogDto;
import dev.kangmin.pawpal.domain.dog.repository.DogRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.global.error.exception.CustomException;
import dev.kangmin.pawpal.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.kangmin.pawpal.global.error.exception.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final MemberService memberService;

    /**
     * 강아지 정보 등록
     * @param member
     * @param dogInfoDto
     */
    @Transactional
    public void generateDogInfo(Member member, DogInfoDto dogInfoDto) {
        //사용자 찾아서
        Dog dog = Dog.builder()
                .name(dogInfoDto.getName())
                .breed(dogInfoDto.getBreed())
                .isNeutralizing(dogInfoDto.isNeutralizing())
                .age(dogInfoDto.getAge())
                .image(dogInfoDto.getImage())
                .member(member)
                .build();

        //강아지 정보 저장
        dogRepository.save(dog);
    }

    /**
     * 강아지 정보 수정
     * @param member
     * @param updateDogDto
     */
    @Transactional
    public void modifyDogInfo(Member member, UpdateDogDto updateDogDto) {
//        Member member = memberService.findMemberByEmail(email);
        //로그인된 사람과 해당 강아지 정보의 주인이 맞는지
        if(!member.getMemberId().equals(updateDogDto.getMemberId())){
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        Dog findDog = findDogByMember(member);
        findDog.modifyInfo(updateDogDto);
    }

    /**
     * 강아지 정보 삭제
     *
     * @param member
     * @param updateDogDto
     */
    @Transactional
    public void deleteDogInfo(Member member, UpdateDogDto updateDogDto) {

        //나의 강아지 정보가 맞는지 검증
        if (!member.getMemberId().equals(updateDogDto.getMemberId())) {
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        findDogByMember(member).change(ExistStatus.DELETED);
    }

    //강아지 정보 찾기
    /**
     * 강아지의 주인으로 찾기
     * @param member
     * @return
     */
    public Dog findDogByMember(Member member) {
        return dogRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, DOG_IS_NOT_EXISTS));
    }

    /**
     * 강아지 ID로 찾기
     * @param dogId
     * @return
     */
    public Dog findDogByDogId(Long dogId) {
        return dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, DOG_IS_NOT_EXISTS));
    }

    /**
     * 사용자와 강아지 ID로 찾기
     * @param memberId
     * @param dogId
     * @return
     */
    public Dog findDogByMemberIdAndDogId(Long memberId, Long dogId) {
        return dogRepository.findByMemberMemberIdAndDogId(memberId, dogId)
                .orElseThrow(() -> new CustomException(BAD_REQUEST, DOG_IS_NOT_EXISTS));
    }

    /**
     * 나의 강아지들 조회
     * @param member
     * @return
     */
    public Page<DogInquiryDto> getMyDogsInfo(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dogRepository.findDogListByMember(member, pageable);
    }


    //query DSL로 밑에 해결하자

    /**
     * 내 강아지의 견종 별 조회
     *
     * @param member
     * @param breed
     * @return
     */
    public Page<DogInquiryDto> getFilteredMyDogInfoByBreed(Member member, String breed, int page, int size) {
        //원하는 견종의 DTO 리스트 반환
        Pageable pageable = PageRequest.of(page, size);
        return dogRepository.findByMemberAndBreed(member, breed, pageable);
    }

    //query DSL로 밑에 해결하자
    /**
     * 사용자의 강아지 나이 순 정렬
     *
     * @param member
     * @param sortBy
     * @return
     */
    public Page<DogInquiryDto> getMyDogInfoOrderByAge(Member member, boolean sortBy, int page, int size) {
        //사용자의 강아지 정보 나이 순 정렬
        Pageable pageable = PageRequest.of(page, size);
        return dogRepository.findByMemberAndSortBy(member, sortBy, pageable);
    }

    /**
     * 사용자의 강아지 세부 정보
     * @param member
     * @param dogId
     * @return
     */
    public DogDetailDto getMyDogDetailInfo(Member member, Long dogId) {
        //강아지 세부 정보 조회
        return DogDetailDto.of(findDogByMemberIdAndDogId(member.getMemberId(), dogId));
    }

}
