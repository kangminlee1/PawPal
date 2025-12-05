package dev.kangmin.pawpal.domain.walk;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.walk.dto.WalkModifyDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Walk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walkId;

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime updateDate;

    //산책 시작 시간
    private LocalDateTime startTime;
    //이동 시간
    private Long duration;
    //사용자 정의 이동 시간 추가 예정

    //이동 거리
    private Double distance;
    //사용자 정의 이동 거리 추가 예정

    //시작점
    private String startPoint;
    //도착점
    private String endPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;


    public void modify(WalkModifyDto modifyDto) {
        this.duration = modifyDto.getDuration();
        this.distance = modifyDto.getDistance();
        this.startPoint = modifyDto.getStartPoint();
        this.endPoint = modifyDto.getEndPoint();
    }
}
