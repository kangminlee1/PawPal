package dev.kangmin.pawpal.domain.member.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDto {

    private String name;
    private String email;
    private String password;
    private String identify;
    private String provider;
    private String providerId;

}
