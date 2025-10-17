package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepositoryCustom {

    Page<DogInquiryDto> findByMemberAndSortBy(Member member, boolean sortBy, Pageable pageable);

    Page<DogInquiryDto> findDogListByMember(Member member, Pageable pageable);

    Page<DogInquiryDto> findByMemberAndBreed(Member member, String breed, Pageable pageable);

}
