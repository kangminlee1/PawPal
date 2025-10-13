package dev.kangmin.pawpal.domain.mylike;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_board_existStatus", columnList = "board_id, existStatus"),
                @Index(name = "idx_board_board", columnList = "member_id, board_id")
        }
)// 1. 게시글 별 좋아요 수 조회 빠르게 2. 특정 회원이 특정 게시글 좋아요 눌렀는지 빠르게 조회
public class MyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long myLikeId;

    @Enumerated(EnumType.STRING)
    private ExistStatus existStatus;

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    public void changed(ExistStatus existStatus){
        this.existStatus = existStatus;
    }
}

