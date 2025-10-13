package dev.kangmin.pawpal.domain.mylike.repository.querydsl;

import dev.kangmin.pawpal.domain.mylike.MyLike;

public interface MyLikeRepositoryCustom {
    MyLike findByMemberEmailAndBoardId(String email, long boardId);
}
