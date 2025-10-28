package dev.kangmin.pawpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling// 스케줄러 사용 가능
@SpringBootApplication
@EnableAsync
public class PawpalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PawpalApplication.class, args);
	}

}
