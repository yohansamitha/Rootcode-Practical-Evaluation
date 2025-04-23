package com.rootcode.practicalevaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PracticalEvaluationApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticalEvaluationApplication.class, args);
    }

}
