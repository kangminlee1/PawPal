package dev.kangmin.pawpal.domain.board;

import dev.kangmin.pawpal.domain.board.dto.GenerateBoardDto;
import dev.kangmin.pawpal.domain.board.dto.ModifyBoardDto;
import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.member.Member;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;
    private String content;
    //이미지를 내용에 어떻게 포함시킬지 여부는 추후 생각

    @Enumerated(EnumType.STRING)
    private ExistStatus existStatus;

    private int viewCount;


    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;


    //연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "board")
    private List<MyLike> myLikeList;


    public void modifyBoard(ModifyBoardDto modifyBoardDto) {
        this.title = modifyBoardDto.getTitle();
        this.content = modifyBoardDto.getContent();
    }

    public void deleteBoard() {
        this.existStatus = ExistStatus.DELETED;
    }
}
