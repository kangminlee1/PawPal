package dev.kangmin.pawpal.domain.dog.dto;

import dev.kangmin.pawpal.domain.dog.Dog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DogInfoDto {

    private String name;
    private String breed;
    private boolean isNeutralizing;
    private int age;
    private String image;


    public Dog toEntity(DogInfoDto dogInfoDto) {
        return Dog.builder()
                .name(dogInfoDto.name)
                .breed(dogInfoDto.breed)
                .isNeutralizing(dogInfoDto.isNeutralizing)
                .age(dogInfoDto.age)
                .image(dogInfoDto.image)
                .build();

    }

}
