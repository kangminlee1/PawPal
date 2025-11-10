package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.dog.dto.DogNameIdDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DogRepositoryCustom {

    Page<DogInquiryDto> findByMemberAndSortBy(Member member, boolean sortBy, Pageable pageable);

    Page<DogInquiryDto> findDogListByMember(Member member, Pageable pageable);

    Page<DogInquiryDto> findByMemberAndBreed(Member member, String breed, Pageable pageable);

    List<DogNameIdDto> findDogNameIdListByMember(Member member);

    List<Dog> findAllDogByMember(Member member);

    Optional<Dog> findByDogId(Long dogId);

    Optional<Dog> findByMemberMemberIdAndDogId(Long memberId, Long dogId);

//    List<Dog> findAllByNextHealthCheckDateBefore(LocalDateTime localDateTime);
}

