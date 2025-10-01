package dev.kangmin.pawpal.domain.dog.service;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.dto.DogDetailDto;
import dev.kangmin.pawpal.domain.dog.dto.DogInfoDto;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.dog.repository.DogRepository;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.member.service.MemberService;
import dev.kangmin.pawpal.golbal.error.exception.CustomException;
import dev.kangmin.pawpal.golbal.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.kangmin.pawpal.golbal.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final MemberService memberService;

    //강아지 정보 입력
    public void generateDogInfo(String email, DogInfoDto dogInfoDto) {
        //사용자 찾아서
        Member member = memberService.findMemberByEmail(email);

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

    //강아지 정보 수정
    public void modifyDogInfo(String email, DogInfoDto dogInfoDto) {
        Member member = memberService.findMemberByEmail(email);
        Dog findDog = findDogByMember(member);
        findDog.modifyInfo(dogInfoDto);
    }

    //강아지 정보 찾기
    public Dog findDogByMember(Member member) {
        return dogRepository.findByMember(member)
                .orElseThrow(()-> new CustomException(DOG_IS_NOT_EXISTS));
    }

    public Dog findDogByDogId(Long dogId) {
        return dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new CustomException(DOG_IS_NOT_EXISTS));
    }
    public Dog findDogByMemberEmailAndDogId(String email, Long dogId) {
        return dogRepository.findByDogId(dogId)
                .orElseThrow(() -> new CustomException(DOG_IS_NOT_EXISTS));
    }

    //사용자의 강아지 정보 삭제
    public void deleteDogInfo(String email, DogInfoDto dogInfoDto){
        Member member = memberService.findMemberByEmail(email);

        findDogByMember(member).change(ExistStatus.DELETED);
    }


    //나의 강아지 전체  목록
    public List<DogInquiryDto> getMyDogsInfo(String email) {
        return dogRepository.findByMemberEmail(email).stream()
                .map(DogInquiryDto::of)
                .toList();
    }

    //사용자의 원하는 견종의 정보
    //query DSL로 밑에 해결하자
    public List<DogInquiryDto> getFilteredMyDogInfoByBreed(String email, String breed) {

        //원하는 견종의 DTO 리스트 반환
        return dogRepository.findByMemberEmailAndBreed(email, breed).stream()
                .map(DogInquiryDto::of)
                .toList();
    }

    //사용자의 강아지들의 나이 순 정렬
    //query DSL로 밑에 해결하자
    public List<DogInquiryDto> getMyDogInfoOrderByAge(String email, boolean sortBy) {
        //사용자의 강아지 정보 나이 순 정렬
        return dogRepository.findByMemberEmailAndSortBy(email, sortBy).stream()
                .map(DogInquiryDto::of)
                .toList();
    }

    //나의 강아지 세부 사항
    public DogDetailDto getMyDogDetailInfo(String email, Long dogId) {
        //강아지 세부 정보 조회
        return DogDetailDto.of(findDogByMemberEmailAndDogId(email, dogId));
    }

}
