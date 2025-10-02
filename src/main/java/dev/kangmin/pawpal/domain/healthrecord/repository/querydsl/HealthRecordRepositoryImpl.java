package dev.kangmin.pawpal.domain.healthrecord.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.kangmin.pawpal.domain.healthrecord.HealthRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HealthRecordRepositoryImpl implements HealthRecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<HealthRecord> findByMemberEmailAndDogIdAndHealthRecordId(String email, Long dogId, Long healthRecordId) {
        return Optional.empty();
    }

    @Override
    public Optional<HealthRecord> findByMemberEmailAndHealthRecordId(String email, Long healthRecordId) {
        return Optional.empty();
    }

    @Override
    public List<HealthRecord> findByMemberEmail(String email) {
        return List.of();
    }

    @Override
    public List<HealthRecord> findByMemberEmailOrderByCreateDate(String email, boolean sortBy) {
        return List.of();
    }

    @Override
    public List<HealthRecord> findByMemberEmailAndDogName(String email, String dogName) {
        return List.of();
    }
}
