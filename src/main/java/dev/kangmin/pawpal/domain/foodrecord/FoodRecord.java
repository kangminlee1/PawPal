package dev.kangmin.pawpal.domain.foodrecord;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.enums.FoodType;
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
public class FoodRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodRecordId;

    private String name;

    @Enumerated(EnumType.STRING)
    private FoodType type;

    //선호도
    private boolean preference;

    private int amount;

    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;
}
