package co.kr.toybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToyBackendApplication.class, args);
    }

}
