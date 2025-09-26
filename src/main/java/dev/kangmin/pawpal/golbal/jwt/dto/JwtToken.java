package dev.kangmin.pawpal.golbal.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtToken {
    @JsonProperty("access_token")
    private String jwtAccessToken;

    @JsonProperty("refresh_token")
    private String jwtRefreshToken;

    @Builder
    public JwtToken(String jwtAccessToken, String jwtRefreshToken) {
        this.jwtAccessToken = jwtAccessToken;
        this.jwtRefreshToken = jwtRefreshToken;
    }
}
