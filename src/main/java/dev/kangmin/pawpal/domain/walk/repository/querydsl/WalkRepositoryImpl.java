package dev.kangmin.pawpal.domain.walk.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.walk.Walk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public void deleteByWalkId(Long walkId){
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
}
