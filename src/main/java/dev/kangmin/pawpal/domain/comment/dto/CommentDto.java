package dev.kangmin.pawpal.domain.comment.dto;

import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommentDto {

    private Long commentId;
    @NotBlank
    private Long memberId;// 멤버 검증
    @NotNull
    private String content;
    private LocalDateTime createDate;
    @NotBlank
    private String memberName;

    //대댓글
    private List<CommentDto> children;
    @NotBlank
    private Long parentId;

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .content(comment.getContent())
                .createDate(comment.getCreateDate())
                .memberName(comment.getMember().getName())
                .parentId(comment.getParent() != null ? comment.getParent().getCommentId() : null)
                .children(ofList(comment.getChildren()))
                .build();
    }

    public static List<CommentDto> ofList(List<Comment> commentList) {
        if (commentList == null) {
            return List.of();
        }

        // 부모의 댓글만 filtering
        List<Comment> parentComments = commentList.stream()
                .filter(comment -> comment.getParent() == null && comment.getExistStatus() == ExistStatus.EXISTS)
                .toList();

        //각 부모 댓글에 대해 자식 댓글 리스트 생성
        return parentComments.stream()
                .map(parent -> CommentDto.builder()
                        .content(parent.getContent())
                        .createDate(parent.getCreateDate())
                        .memberName(parent.getMember().getName())
                        .parentId(null)

                        //부모 댓글의 children은 parent가 이 댓글인 자식들만 매핑함
                        .children(commentList.stream()
                                .filter(comment -> comment.getParent() != null
                                        && comment.getParent().getCommentId().equals(parent.getCommentId())
                                        && comment.getExistStatus() == ExistStatus.EXISTS)
                                .map(CommentDto::of)
                                .toList())
                        .build())
                .toList();
    }

}
