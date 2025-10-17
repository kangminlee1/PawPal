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
public class DogInquiryDto {

    private Long memberId;
    private Long dogId;
    private String name;
    private String breed;
    private int age;

    public static DogInquiryDto of(Dog dog) {
        return DogInquiryDto.builder()
                .memberId(dog.getMember().getMemberId())
                .dogId(dog.getDogId())
                .breed(dog.getBreed())
                .age(dog.getAge())
                .name(dog.getName())
                .build();
    }
}
