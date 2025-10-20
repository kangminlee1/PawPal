package dev.kangmin.pawpal.domain.healthrecord.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import dev.kangmin.pawpal.domain.healthrecord.dto.HealthInquiryDto;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.domain.dog.QDog.dog;
import static dev.kangmin.pawpal.domain.healthrecord.QHealthRecord.healthRecord;
import static dev.kangmin.pawpal.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class HealthRecordRepositoryImpl implements HealthRecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<HealthRecord> findByMemberAndDogIdAndHealthRecordId(Member nowMember, Long dogId, Long healthRecordId) {
        return Optional.ofNullable(
                queryFactory
                        .select(healthRecord)
                        .from(healthRecord)
                        .join(healthRecord.dog, dog).fetchJoin()
                        .join(dog.member, member).fetchJoin()
                        .where(healthRecord.dog.member.eq(nowMember),
                                healthRecord.dog.dogId.eq(dogId),
                                healthRecord.healthRecordId.eq(healthRecordId))
                        .fetchOne()
        );
    }

    @Override
    public Optional<HealthRecord> findByMemberAndHealthRecordId(Member nowMember, Long healthRecordId) {
        return Optional.ofNullable(
                queryFactory
                        .select(healthRecord)
                        .from(healthRecord)
                        .join(healthRecord.dog, dog).fetchJoin()
                        .join(dog.member, member).fetchJoin()
                        .where(
                                dog.member.eq(nowMember),
                                healthRecord.healthRecordId.eq(healthRecordId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<HealthInquiryDto> findByMember(Member member, Pageable pageable) {

        List<HealthInquiryDto> healthInquiryDtoList = queryFactory
                .select(
                        Projections.constructor(HealthInquiryDto.class,
                                healthRecord.dog.dogId,
                                healthRecord.healthRecordId,
                                healthRecord.dog.name,
                                healthRecord.content,
                                healthRecord.createDate
                        )
                )
                .from(healthRecord)
                .where(
                        healthRecord.dog.member.eq(member)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(healthRecord.count())
                .from(healthRecord)
                .where(
                        healthRecord.dog.member.eq(member)
                );

        return PageableExecutionUtils.getPage(healthInquiryDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<HealthInquiryDto> findByMemberOrderByCreateDate(Member member, boolean sortBy, Pageable pageable) {

        List<HealthInquiryDto> healthInquiryDtoList = queryFactory
                .select(Projections.constructor(
                                HealthInquiryDto.class,
                                healthRecord.dog.dogId,
                                healthRecord.healthRecordId,
                                healthRecord.dog.name,
                                healthRecord.content,
                                healthRecord.createDate
                        )
                )
                .from(healthRecord)
                .where(
                        healthRecord.dog.member.eq(member)
                )
                .orderBy(
                        sortBy ?
                                healthRecord.createDate.desc() : healthRecord.createDate.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(healthRecord.count())
                .from(healthRecord)
                .where(healthRecord.dog.member.eq(member));

        return PageableExecutionUtils.getPage(healthInquiryDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<HealthInquiryDto> findByMemberAndDogName(Member member, String dogName, Pageable pageable) {
        List<HealthInquiryDto> healthInquiryDtoList = queryFactory
                .select(Projections.constructor(
                                HealthInquiryDto.class,
                                healthRecord.dog.dogId,
                                healthRecord.healthRecordId,
                                healthRecord.dog.name,
                                healthRecord.content,
                                healthRecord.createDate
                        )
                )
                .from(healthRecord)
                .where(
                        healthRecord.dog.member.eq(member),
                        healthRecord.dog.name.eq(dogName)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(healthRecord.count())
                .from(healthRecord)
                .where(
                        healthRecord.dog.member.eq(member),
                        healthRecord.dog.name.eq(dogName)
                );

        return PageableExecutionUtils.getPage(healthInquiryDtoList, pageable, countQuery::fetchOne);
    }
}
