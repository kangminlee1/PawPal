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
public class FoodInfoDto {

    private Long foodRecordId;
    private Long dogId;
    private String dogName;
    private String foodName;
    private Date createDate;

    public static FoodInfoDto of(FoodRecord foodRecord) {
        return FoodInfoDto.builder()
                .foodRecordId(foodRecord.getFoodRecordId())
                .dogId(foodRecord.getDog().getDogId())
                .dogName(foodRecord.getDog().getName())
                .foodName(foodRecord.getName())
                .createDate(foodRecord.getCreateDate())
                .build();
    }

}
