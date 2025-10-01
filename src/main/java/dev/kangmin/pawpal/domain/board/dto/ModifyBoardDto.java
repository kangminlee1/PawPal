package dev.kangmin.pawpal.domain.board.dto;

import dev.kangmin.pawpal.domain.enums.ExistStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyBoardDto {

    private Long boardId;
    private String title;
    private String content;

}
