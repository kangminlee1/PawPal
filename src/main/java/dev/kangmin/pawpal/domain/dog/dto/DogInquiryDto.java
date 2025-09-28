package dev.kangmin.pawpal.domain.dog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DogInquiryDto {

    private Long dogId;
    private String name;
    private String breed;
    private int age;

}
