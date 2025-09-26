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
}

