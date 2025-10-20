package dev.kangmin.pawpal.domain.healthrecord.repository.querydsl;

import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import dev.kangmin.pawpal.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepositoryCustom {
    Optional<HealthRecord> findByMemberAndDogIdAndHealthRecordId(Member member, Long dogId, Long healthRecordId);

    Optional<HealthRecord> findByMemberAndHealthRecordId(Member member, Long healthRecordId);

    Page<HealthInquiryDto> findByMember (Member member, Pageable pageable);

    Page<HealthInquiryDto> findByMemberOrderByCreateDate(Member member, boolean sortBy, Pageable pageable);

    Page<HealthInquiryDto> findByMemberAndDogName(Member member, String dogName, Pageable pageable);
}
