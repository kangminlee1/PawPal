package dev.kangmin.pawpal.domain.mylike.repository;

import dev.kangmin.pawpal.domain.mylike.MyLike;
import dev.kangmin.pawpal.domain.mylike.repository.querydsl.MyLikeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyLikeRepository  extends JpaRepository<MyLike, Long>, MyLikeRepositoryCustom {
}
