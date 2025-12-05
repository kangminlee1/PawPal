package dev.kangmin.pawpal.domain.walk.repository.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.walk.Walk;
import dev.kangmin.pawpal.domain.walk.dto.WalkStatisticsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static dev.kangmin.pawpal.domain.dog.QDog.dog;
import static dev.kangmin.pawpal.domain.member.QMember.member;
import static dev.kangmin.pawpal.domain.walk.QWalk.walk;

@Repository
@Slf4j
@RequiredArgsConstructor
public class WalkRepositoryImpl implements WalkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Walk> findByWalkId(Long walkId) {
        return Optional.ofNullable(
                queryFactory
                        .select(walk)
                        .from(walk)
                        .where(walk.walkId.eq(walkId))
                        .fetchOne()
        );
    }

    @Override
    public void deleteByWalkId(Long walkId) {
        queryFactory
                .delete(walk)
                .where(walk.walkId.eq(walkId))
                .execute();
    }

    @Override
    public Optional<Walk> findByWalkIdAndMemberId(Long walkId, Long memberId) {
        return Optional.ofNullable(
                queryFactory
                        .select(walk)
                        .from(walk)
                        .join(walk.dog, dog).fetchJoin()
                        .join(dog.member, member).fetchJoin()
                        .where(walk.walkId.eq(walkId),
                                member.memberId.eq(memberId))
                        .fetchOne()
        );
    }

    @Override
    public WalkStatisticsDto findWalksByOwnerAndDogWithinPeriod(Long memberId, Long dogId, LocalDateTime start, LocalDateTime end) {

        // 1) 시간대 CASE 식을 하나의 변수로 추출
        var timeZoneCase = Expressions.stringTemplate(
                "CASE " +
                        "WHEN HOUR({0}) BETWEEN 0 AND 5 THEN 'dawn' " +
                        "WHEN HOUR({0}) BETWEEN 6 AND 10 THEN 'morning' " +
                        "WHEN HOUR({0}) BETWEEN 11 AND 14 THEN 'lunch' " +
                        "WHEN HOUR({0}) BETWEEN 15 AND 18 THEN 'afternoon' " +
                        "WHEN HOUR({0}) BETWEEN 19 AND 23 THEN 'evening' " +
                        "END",
                walk.startTime
        );


        List<Tuple> timeZoneResult = queryFactory
                .select(
                        timeZoneCase.as("timezone"),
                        walk.count()
                )
                .from(walk)
                .where(
                        walk.dog.dogId.eq(dogId),
                        walk.dog.member.memberId.eq(memberId),
                        walk.startTime.between(start, end)
                )
                .groupBy(timeZoneCase)
                .fetch();

        Map<String, Long> timeZonePattern = timeZoneResult.stream()
                .collect(Collectors.toMap(
                        t -> t.get(0, String.class),
                        t -> t.get(1, Long.class),
                        Long::sum
                ));


        WalkStatisticsDto result = queryFactory
                .select(Projections.fields(
                        WalkStatisticsDto.class,
                        walk.count().as("walkCount"),
                        walk.distance.sum().as("totalDistance"),
                        walk.duration.avg().as("averageDuration"),

                        // achievementRate : (달성한 산책수 / 30) * 100
                        Expressions.numberTemplate(Double.class,
                                " (SUM(CASE WHEN {0} >= 1000 THEN 1 ELSE 0 END) * 100.0) / 30 ",
                                walk.distance
                        ).as("achievementRate"),

                        ExpressionUtils.as(Expressions.constant(start), "startDate"),
                        ExpressionUtils.as(Expressions.constant(end), "endDate")
                ))
                .from(walk)
                //통계에서는 괜히 성능이 떨어짐.
//                .join(walk.dog, dog).fetchJoin()
//                .join(dog.member, member).fetchJoin()
                .where(
                        member.memberId.eq(memberId),
                        walk.dog.dogId.eq(dogId),
                        walk.startTime.between(start, end)
                )
                .fetchOne();

        if (result != null) {
            result.setTimeZonePattern(timeZonePattern);
        }

        return result;
    }

}
