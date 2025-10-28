package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.dog.QDog;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.dog.dto.DogNameIdDto;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.domain.dog.QDog.dog;
import static dev.kangmin.pawpal.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class DogRepositoryImpl implements DogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    //나이순 정렬(true == 오름차순, false 내림차순)
    @Override
    public Page<DogInquiryDto> findByMemberAndSortBy(Member member, boolean sortBy, Pageable pageable) {
        List<DogInquiryDto> dogInquiryDtoList = queryFactory
                .select(Projections.constructor(DogInquiryDto.class,
                        dog.dogId,
                        dog.name,
                        dog.breed,
                        dog.age)
                )
                .from(dog)
                .where(
                        dog.member.eq(member)
                )
//                .groupBy(dog.dogId, dog.name, dog.breed, dog.age)
                .orderBy(
                        sortBy ? dog.age.asc() : dog.age.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dog.count())
                .from(dog)
                .where(dog.member.eq(member));

        return PageableExecutionUtils.getPage(dogInquiryDtoList, pageable, countQuery::fetchOne);
    }

    //사용자의 강아지 조회
    @Override
    public Page<DogInquiryDto> findDogListByMember(Member member, Pageable pageable) {

        List<DogInquiryDto> dogInquiryDtoList = queryFactory
                .select(Projections.constructor(DogInquiryDto.class,
                        dog.dogId,
                        dog.name,
                        dog.breed,
                        dog.age)
                )
                .from(dog)
                .where(
                        dog.member.eq(member)
                )
//                .groupBy(dog.dogId, dog.name, dog.breed,dog.age)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dog.count())
                .from(dog)
                .where(
                        dog.member.eq(member)
                );

        return PageableExecutionUtils.getPage(dogInquiryDtoList, pageable, countQuery::fetchOne);
    }

    //사용자의 강아지 조회 with 견종
    @Override
    public Page<DogInquiryDto> findByMemberAndBreed(Member member, String breed, Pageable pageable) {
        List<DogInquiryDto> dogInquiryDtoList = queryFactory
                .select(Projections.constructor(DogInquiryDto.class,
                        dog.dogId,
                        dog.name,
                        dog.breed,
                        dog.age)
                )
                .from(dog)
                .where(
                        dog.member.eq(member),
                        dog.breed.eq(breed)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dog.count())
                .from(dog)
                .where(
                        dog.member.eq(member),
                        dog.breed.eq(breed)
                );

        return PageableExecutionUtils.getPage(dogInquiryDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public List<DogNameIdDto> findDogNameIdListByMember(Member member) {
        return queryFactory
                .select(
                        Projections.constructor(
                                DogNameIdDto.class,
                                dog.dogId,
                                dog.name
                        )
                )
                .from(dog)
                .where(
                        dog.member.eq(member)
                )
                .fetch();
    }

    @Override
    public List<Dog> findAllDogByMember(Member nowMember) {
        return queryFactory
                .select(dog)
                .from(dog)
                .leftJoin(dog.member, member).fetchJoin()
                .where(
                        dog.member.eq(nowMember)
                )
                .fetch();
    }

    @Override
    public Optional<Dog> findByDogId(Long dogId) {
        return Optional.ofNullable(
                queryFactory
                        .select(dog)
                        .from(dog)
                        .leftJoin(dog.member, member).fetchJoin()
                        .where(
                                dog.dogId.eq(dogId)
                        )
                        .fetchOne()
        );

    }

    @Override
    public Optional<Dog> findByMemberMemberIdAndDogId(Long memberId, Long dogId) {
        return Optional.ofNullable(
                queryFactory
                        .select(dog)
                        .from(dog)
                        .leftJoin(dog.member, member).fetchJoin()
                        .where(
                                dog.member.memberId.eq(memberId),
                                dog.dogId.eq(dogId)
                        )
                        .fetchOne()
        );
    }

    @Override
    public List<Dog> findAllByNextHealthCheckDateBefore(LocalDateTime now) {
        return queryFactory
                .select(dog)
                .from(dog)
                .leftJoin(dog.member, member).fetchJoin()
                .where(
                        dog.age.loe(1).and(dog.nextHealthCheckDate.loe(now))
                                .or(dog.age.between(2, 6).and(dog.nextHealthCheckDate.loe(now)))
                                .or(dog.age.goe(7).and(dog.nextHealthCheckDate.loe(now)))
                )
                .fetch();
    }

}
