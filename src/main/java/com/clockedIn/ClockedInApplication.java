package com.clockedIn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ClockedInApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(ClockedInApplication.class, args);

    }


}