package dev.kangmin.pawpal.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class GenerateDto {

    private String name;
    private String email;
    private String password;
    private String identify;
    private String provider;
    private String providerId;

}
