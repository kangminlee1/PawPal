package dev.kangmin.pawpal.domain.dog.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.dto.*;
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

    //강아지 이름, ID 값은 메인 페이지 첫 진입 시 내려주고,
    //강아지 정보 수정 시 다시 프론트가 해당 API를 재호출해서 다시 받는 구조라는 설정
    //이거 만드셈

    //강아지 정보 등록 및 수정 시 사용자의 모든 강아지의 이름, id 값 반환

    /**
     * 나의 모든 강아지 이름, id 값
     * @param member
     * @return
     */
    public List<DogNameIdDto> getDogNameAndDogIdList(Member member) {
        return dogRepository.findDogNameIdListByMember(member);
    }

    /**
     * 강아지 정보 등록
     * @param member
     * @param dogInfoDto
     */
    @Transactional
    public List<DogNameIdDto> generateDogInfo(Member member, DogInfoDto dogInfoDto) {
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

        return getDogNameAndDogIdList(member);
    }

    /**
     * 강아지 정보 수정
     * @param member
     * @param updateDogDto
     */
    @Transactional
    public List<DogNameIdDto> modifyDogInfo(Member member, UpdateDogDto updateDogDto) {
        //로그인된 사람과 해당 강아지 정보의 주인이 맞는지
        Dog findDog = findDogByDogId(updateDogDto.getDogId());

        if(!findDog.getMember().getMemberId().equals(member.getMemberId())){
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        findDog.modifyInfo(updateDogDto);
        return getDogNameAndDogIdList(member);
    }

    /**
     * 강아지 정보 삭제
     *
     * @param member
     * @param updateDogDto
     */
    @Transactional
    public List<DogNameIdDto> deleteDogInfo(Member member, UpdateDogDto updateDogDto) {
        Dog findDog = findDogByDogId(updateDogDto.getDogId());
        //나의 강아지 정보가 맞는지 검증
        if (!findDog.getMember().getMemberId().equals(member.getMemberId())) {
            throw new CustomException(FORBIDDEN, DOG_OWNER_MISMATCH);
        }

        findDogByMemberIdAndDogId(member.getMemberId(), updateDogDto.getDogId()).change(ExistStatus.DELETED);
        return getDogNameAndDogIdList(member);
    }

    //강아지 정보 찾기
    /**
     * 사용자의 모든 강아지 찾기
     * @param member
     * @return
     */
    public List<Dog> findAllDogByMember(Member member) {
        return dogRepository.findAllDogByMember(member);
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
