package dev.kangmin.pawpal.global.redis.dto;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetJwtTokenDto {
    private String jwtAccessToken;
    private String jwtRefreshToken;

}
