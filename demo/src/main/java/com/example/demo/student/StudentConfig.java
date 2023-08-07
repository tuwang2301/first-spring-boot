package com.example.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student tuwang = new Student(
                    "TuWang",
                    LocalDate.of(2003, Month.JANUARY, 23),
                    "quangtu2301@gmail.com"
            );
            Student alex = new Student(
                    "Alex",
                    LocalDate.of(2000, Month.JANUARY, 22),
                    "alex@gmail.com"
            );
            studentRepository.saveAll(
                    List.of(tuwang,alex)
            );
        };
    }
}
