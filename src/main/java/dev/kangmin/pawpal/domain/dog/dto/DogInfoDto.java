package dev.kangmin.pawpal.domain.dog.dto;

import dev.kangmin.pawpal.domain.dog.Dog;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DogInfoDto {

    @NotBlank
    private Long memberId;
    @NotBlank
    private String name;
    @NotBlank
    private String breed;
    @NotBlank
    private boolean isNeutralizing;
    @NotBlank
    private int age;
    private String image;


//    public Dog toEntity(DogInfoDto dogInfoDto) {
//        return Dog.builder()
//                .name(dogInfoDto.name)
//                .breed(dogInfoDto.breed)
//                .isNeutralizing(dogInfoDto.isNeutralizing)
//                .age(dogInfoDto.age)
//                .image(dogInfoDto.image)
//                .build();
//
//    }

}
