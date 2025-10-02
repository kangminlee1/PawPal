package dev.kangmin.pawpal.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI pawPal() {
        return new OpenAPI()
                .info(new Info().title("발바닥 친구(Paw Pal")
                        .description("Paw Pal API 명세서<br> +" +
                                "서버에서 핸들링한 오류 ex)" +
                                " key: errorMessage , v : 사용자가 존재하지않습니다.  형식 <br>")
                        .version("V1.0")
                );
    }
}
