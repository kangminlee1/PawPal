package dev.kangmin.pawpal.domain.dog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogNameIdDto {
    private Long dogId;
    private String dogName;
}
