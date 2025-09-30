package dev.kangmin.pawpal.domain.member;

import dev.kangmin.pawpal.domain.board.Board;
import dev.kangmin.pawpal.domain.comment.Comment;
import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.enums.ExistStatus;
import dev.kangmin.pawpal.domain.enums.MemberRole;
import dev.kangmin.pawpal.domain.member.dto.ModifyMemberDto;
import dev.kangmin.pawpal.domain.mylike.MyLike;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String identify;
    @Column(nullable = false)
    private String provider;
    @Column(nullable = false)
    private String providerId;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(STRING)
    @Column(nullable = false)
    private ExistStatus existStatus;

    @CreatedDate
    private Date createAt;
    @LastModifiedDate
    private Date updateAt;


    @OneToMany(mappedBy = "member")
    private List<Dog> dogList;

    @OneToMany(mappedBy = "member")
    private List<Board> boardList;

    @OneToMany(mappedBy = "member")
    private List<MyLike> myLikeList;

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList;


    //dirty checking
    public void modifyMember(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void changeStatus(ExistStatus status) {
        this.existStatus = status;
    }

    //of : entity -> dto


}
