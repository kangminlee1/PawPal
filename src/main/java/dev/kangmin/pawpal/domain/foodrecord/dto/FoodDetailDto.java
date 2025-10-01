package dev.kangmin.pawpal.domain.foodrecord.dto;

import dev.kangmin.pawpal.domain.enums.FoodType;
import dev.kangmin.pawpal.domain.foodrecord.FoodRecord;
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

    //entity -> dto
    public static FoodDetailDto of(FoodRecord foodRecord) {
        return FoodDetailDto.builder()
                .foodRecordId(foodRecord.getFoodRecordId())
                .dogId(foodRecord.getDog().getDogId())
                .memberName(foodRecord.getDog().getMember().getName())
                .dogName(foodRecord.getDog().getName())
                .foodName(foodRecord.getName())
                .type(foodRecord.getType())
                .amount(foodRecord.getAmount())
                .preference(foodRecord.getPreference())
                .createDate(foodRecord.getCreateDate())
                .build();
    }

}
