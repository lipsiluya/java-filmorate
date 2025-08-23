package ru.yandex.practicum.filmorate;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println(">>> >>> >>> Filmorate STARTED <<< <<< <<<");
    }
}