package dev.kangmin.pawpal.domain.foodrecord;

import dev.kangmin.pawpal.domain.dog.Dog;
import dev.kangmin.pawpal.domain.enums.FoodType;
import dev.kangmin.pawpal.domain.foodrecord.dto.ModifyFoodDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType type;

    //선호도 1~10
    @Min(0)
    @Max(10)
    @Column(nullable = false)
    private int preference;
    @Column(nullable = false)
    private int amount;

    @CreatedDate
    @Column(nullable = false)
    private Date createDate;
    @LastModifiedDate
    @Column(nullable = false)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogId")
    private Dog dog;


    public void modifyFoodRecord(ModifyFoodDto modifyFoodDto) {
        this.amount = modifyFoodDto.getAmount();
        this.type = modifyFoodDto.getType();
        this.preference = modifyFoodDto.getPreference();
        this.name = modifyFoodDto.getName();
    }
}
