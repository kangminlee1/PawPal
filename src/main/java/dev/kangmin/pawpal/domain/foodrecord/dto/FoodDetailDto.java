package dev.kangmin.pawpal.domain.foodrecord.dto;

import dev.kangmin.pawpal.domain.enums.FoodType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodDetailDto {

    private Long foodRecordId;
    private Long dogId;
    private String memberName;
    private String dogName;
    private String foodName;
    private FoodType type;
    private int preference;
    private int amount;
    private Date createDate;

}
