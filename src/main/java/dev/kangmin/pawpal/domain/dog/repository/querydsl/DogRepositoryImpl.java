package dev.kangmin.pawpal.domain.dog.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.dog.QDog;
import dev.kangmin.pawpal.domain.dog.dto.DogInquiryDto;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.kangmin.pawpal.domain.dog.QDog.dog;

@Repository
@RequiredArgsConstructor
public class DogRepositoryImpl implements DogRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    //나이순 정렬(true == 오름차순, false 내림차순)
    @Override
    public Page<DogInquiryDto> findByMemberAndSortBy(Member member, boolean sortBy, Pageable pageable) {
        List<DogInquiryDto> dogInquiryDtoList = queryFactory
                .select(Projections.constructor(DogInquiryDto.class,
                        dog.member.memberId,
                        dog.dogId,
                        dog.name,
                        dog.breed,
                        dog.age)
                )
                .from(dog)
                .where(
                        dog.member.eq(member)
                )
                .groupBy(dog.member.memberId, dog.dogId, dog.name, dog.breed, dog.age)
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
                        dog.member.memberId,
                        dog.dogId,
                        dog.name,
                        dog.breed,
                        dog.age)
                )
                .from(dog)
                .where(dog.member.eq(member))
                .groupBy(dog.member.memberId, dog.dogId, dog.name, dog.breed,dog.age)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dog.count())
                .from(dog)
                .where();

        return PageableExecutionUtils.getPage(dogInquiryDtoList, pageable, countQuery::fetchOne);
    }

    //사용자의 강아지 조회 with 견종
    @Override
    public Page<DogInquiryDto> findByMemberAndBreed(Member member, String breed, Pageable pageable) {
        List<DogInquiryDto> dogInquiryDtoList = queryFactory
                .select(Projections.constructor(DogInquiryDto.class,
                        dog.member.memberId,
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
}
