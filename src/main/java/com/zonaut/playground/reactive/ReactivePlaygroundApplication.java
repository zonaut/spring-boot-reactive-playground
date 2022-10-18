package com.zonaut.playground.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class ReactivePlaygroundApplication {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ReactivePlaygroundApplication.class);
        application.run(args);
    }
}
