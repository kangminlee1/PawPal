package dev.kangmin.pawpal.domain.dog.dto;

import dev.kangmin.pawpal.domain.dog.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogDetailDto {

    private Long dogId;
    private String name;
    private boolean isNeutralizing;
    private int age;
    private String image;

    public static DogDetailDto of(Dog dog) {
        return DogDetailDto.builder()
                .dogId(dog.getDogId())
                .age(dog.getAge())
                .name(dog.getName())
                .isNeutralizing(dog.isNeutralizing())
                .image(dog.getImage())
                .build();
    }

}
