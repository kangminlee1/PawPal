package dev.kangmin.pawpal.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.comment.dto.CommentDto;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailDto {

    private Long boardId;
    private String title;
    private String memberName;
    @JsonFormat(pattern = "yyyy-mm-dd", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    private String content;
    private int viewCount;

    private List<CommentDto> commentDtoList;
//    private MyLike myLike;

    public static BoardDetailDto of(Board board) {
        return BoardDetailDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .memberName(board.getMember().getName())
                .createDate(board.getCreateDate())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .commentDtoList(CommentDto.ofList(board.getCommentList()))
                .build();

    }

}
