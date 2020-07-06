package com.sovesky.sarscov2tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SarsCov2TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SarsCov2TrackerApplication.class, args);
    }

}
