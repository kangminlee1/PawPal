package dev.kangmin.pawpal.domain.foodrecord.repository.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodDetailDto;
import dev.kangmin.pawpal.domain.foodrecord.dto.FoodInfoDto;
import dev.kangmin.pawpal.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.kangmin.pawpal.domain.dog.QDog.dog;
import static dev.kangmin.pawpal.domain.foodrecord.QFoodRecord.foodRecord;
import static dev.kangmin.pawpal.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class FoodRecordRepositoryImpl implements FoodRecordRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<FoodRecord> findByMemberAndFoodRecordId(Member nowMember, Long foodRecordId) {
        return Optional.ofNullable(
                queryFactory
                        .select(foodRecord)
                        .from(foodRecord)
                        .join(foodRecord.dog.member, member).fetchJoin()
                        .where(
                                foodRecord.foodRecordId.eq(foodRecordId),
                                foodRecord.dog.member.eq(nowMember)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<FoodInfoDto> findFoodInfoDtoListByMember(Member nowMember, Pageable pageable) {
        List<FoodInfoDto> foodInfoDtoList = queryFactory
                .select(
                        Projections.constructor(
                                FoodInfoDto.class,
                                foodRecord.foodRecordId,
                                foodRecord.dog.dogId,
                                foodRecord.dog.name,
                                foodRecord.name,
                                foodRecord.createDate
                        )
                )
                .from(foodRecord)
                .join(dog).on(foodRecord.dog.eq(dog))
                .join(member).on(dog.member.eq(member))
                .where(
                        foodRecord.dog.member.eq(nowMember)
                )
                .groupBy(foodRecord.foodRecordId, foodRecord.dog.dogId, foodRecord.dog.name, foodRecord.name, foodRecord.createDate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(foodRecord.count())
                .from(foodRecord)
                .where(
                        foodRecord.dog.member.eq(nowMember)
                );

        return PageableExecutionUtils.getPage(foodInfoDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FoodInfoDto> findFoodInfoDtoListByMemberAndDogId(Member nowMember, Long dogId, Pageable pageable) {

        List<FoodInfoDto> foodInfoDtoList = queryFactory
                .select(
                        Projections.constructor(
                                FoodInfoDto.class,
                                foodRecord.foodRecordId,
                                foodRecord.dog.dogId,
                                foodRecord.dog.name,
                                foodRecord.name,
                                foodRecord.createDate
                        )
                )
                .from(foodRecord)
                .join(foodRecord.dog, dog)
                .join(dog.member, member)
                .where(
                        foodRecord.dog.member.eq(nowMember),
                        foodRecord.dog.dogId.eq(dogId)
                )
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(foodRecord.count())
                .from(foodRecord)
                .join(foodRecord.dog, dog)
                .join(dog.member, member)
                .where(
                        foodRecord.dog.member.eq(nowMember),
                        foodRecord.dog.dogId.eq(dogId)
                );

        return PageableExecutionUtils.getPage(foodInfoDtoList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FoodInfoDto> findFoodInfoDtoListByMemberAndDogIdOrderByPreference(Member nowMember, Long dogId, Pageable pageable) {
        List<FoodInfoDto> foodRecordList = queryFactory
                .select(
                        Projections.constructor(
                                FoodInfoDto.class,
                                foodRecord.foodRecordId,
                                foodRecord.dog.dogId,
                                foodRecord.dog.name,
                                foodRecord.name,
                                foodRecord.createDate
                        )
                )
                .from(foodRecord)
                .join(foodRecord.dog, dog)
                .join(dog.member, member)
                .where(
                        foodRecord.dog.member.eq(member),
                        foodRecord.dog.dogId.eq(dogId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(foodRecord.count())
                .from(foodRecord)
                .join(foodRecord.dog, dog)
                .join(dog.member, member)
                .where();

        return PageableExecutionUtils.getPage(foodRecordList, pageable, countQuery::fetchOne);
    }

    //보류 수정해야함
    @Override
    public List<FoodRecord> findFoodListByDogId(Long dogId) {

        return null;
    }


    @Override
    public FoodDetailDto findDetailByMemberAndDogIdAndFoodRecordId(Member nowMember, Long dogId, Long foodRecordId) {
        return queryFactory
                .select(
                        Projections.constructor(
                                FoodDetailDto.class,
                                foodRecord.foodRecordId,
                                foodRecord.dog.dogId,
                                foodRecord.dog.member.name,
                                foodRecord.dog.name,
                                foodRecord.name,
                                foodRecord.type,
                                foodRecord.preference,
                                foodRecord.amount,
                                foodRecord.createDate
                        )
                )
                .from(foodRecord)
                .join(foodRecord.dog, dog)
                .join(dog.member, member)
                .where(
                        foodRecord.dog.member.eq(nowMember),
                        foodRecord.dog.dogId.eq(dogId),
                        foodRecord.foodRecordId.eq(foodRecordId)
                )
                .fetchOne();
    }
}
