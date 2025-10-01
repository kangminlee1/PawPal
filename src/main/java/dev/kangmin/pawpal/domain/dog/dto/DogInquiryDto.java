package dev.kangmin.pawpal.domain.dog.dto;

import dev.kangmin.pawpal.domain.dog.Dog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DogInquiryDto {

    private Long dogId;
    private String name;
    private String breed;
    private int age;

    public static DogInquiryDto of(Dog dog) {
        return DogInquiryDto.builder()
                .dogId(dog.getDogId())
                .breed(dog.getBreed())
                .age(dog.getAge())
                .name(dog.getName())
                .build();
    }
}
