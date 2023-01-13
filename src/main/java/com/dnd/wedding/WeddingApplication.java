package com.dnd.wedding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/env.properties")
public class WeddingApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeddingApplication.class, args);
    }

}
