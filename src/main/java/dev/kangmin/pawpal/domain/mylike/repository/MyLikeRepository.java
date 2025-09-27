package dev.kangmin.pawpal.domain.mylike.repository;

import dev.kangmin.pawpal.domain.mylike.MyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyLikeRepository  extends JpaRepository<MyLike, Long> {
}
