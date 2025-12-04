package dev.kangmin.pawpal.domain.walk.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.walk.Walk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class WalkRepositoryImpl implements WalkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Walk> findByWalkId(Long walkId) {
        return Optional.empty();
    }

    @Override
    public void deleteByWalkId(Long walkId){
//        queryFactory
//                .delete(walk)
//                .where(walk.walkId.eq(walkId))
//                .execute();
    }
}
