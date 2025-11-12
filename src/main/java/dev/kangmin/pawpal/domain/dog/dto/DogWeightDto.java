package dev.kangmin.pawpal.domain.dog.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class DogWeightDto {

    private LocalDateTime createDate;
    private double weight;
    private Double weightDeviationPercent;
}
