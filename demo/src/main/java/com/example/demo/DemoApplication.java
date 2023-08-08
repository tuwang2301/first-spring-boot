package com.example.demo;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.classroom.ClassRoomRepository;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;


@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SubjectRepository subjectRepository;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        ClassRoom class1 = ClassRoom.builder()
                .name("A1")
                .maxStudents(30)
                .build();
        ClassRoom class2 = ClassRoom.builder()
                .name("D1")
                .maxStudents(25)
                .build();

        Subject sub1 = Subject.builder()
                .name("Math")
                .credits(4)
                .build();
        Subject sub2 = Subject.builder()
                .name("English")
                .credits(2)
                .build();
        Subject sub3 = Subject.builder()
                .name("Programming")
                .credits(3)
                .build();

        Student student1 = new Student(
                "Nguyen Quang Tu",
                LocalDate.of(2003, Month.JANUARY, 23),
                "quangtu2301@gmail.com"
        );
        Student student2 = new Student(
                "Nguyen Ha Linh",
                LocalDate.of(2000, Month.MAY, 23),
                "halinh2305@gmail.com"
        );
        Student student3 = new Student(
                "Nguyen Quang Anh",
                LocalDate.of(2004, Month.DECEMBER, 2),
                "quanganh0212@gmail.com"
        );

        class1.setStudents(List.of(student1, student2));
        class2.setStudents(List.of(student3));
        sub1.setStudents(List.of(student1,student3));
        sub2.setStudents(List.of(student2,student3));
        sub3.setStudents(List.of(student1,student2,student3));
        classRoomRepository.saveAllAndFlush(List.of(class1,class2));
        subjectRepository.saveAllAndFlush(List.of(sub1,sub2,sub3));

    }
}
