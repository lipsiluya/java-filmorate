package com.example.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.example.controller",
        "com.example.model",
        "com.example.exception"
})
public class FilmorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println(">>> >>> >>> Filmorate STARTED <<< <<< <<<");
    }
}