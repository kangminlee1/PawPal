package dev.kangmin.pawpal.api.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
