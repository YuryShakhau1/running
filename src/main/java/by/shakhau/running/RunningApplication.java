package by.shakhau.running;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories
@SpringBootApplication
public class RunningApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunningApplication.class, args);
    }
}
