package dev.kangmin.pawpal.domain.dog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDogDto {

    @NotBlank
    private Long memberId;
    @NotBlank
    private String breed;
    @NotBlank
    private String name;
    @NotBlank
    private boolean isNeutralizing;
    @NotBlank
    private int age;
    @NotBlank
    private String image;
}
