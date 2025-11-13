package dev.kangmin.pawpal.domain.walk;

import dev.kangmin.pawpal.domain.dog.Dog;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

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

    //이동 시간
    private Long duration;
    //이동 거리
    private Double distance;
    //시작점
    private String startPoint;
    //도착점
    private String endPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;

}
