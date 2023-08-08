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

        Student student1 = Student.builder()
                .name("Nguyen Quang Tu")
                .dob(LocalDate.of(2003, Month.JANUARY, 23))
                .email("quangtu2301@gmail.com")
                .classRoom(class1)
                .build();
        Student student2 = Student.builder()
                .name("Nguyen Ha Linh")
                .dob(LocalDate.of(2000, Month.MAY, 23))
                .email("halinh2305@gmail.com")
                .classRoom(class1)
                .build();
        Student student3 = Student.builder()
                .name("Nguyen Quang Anh")
                .dob(LocalDate.of(2004, Month.DECEMBER, 2))
                .email("quanganh0212@gmail.com")
                .classRoom(class2)
                .build();

        class1.setStudents(List.of(student1, student2));
        class2.setStudents(List.of(student3));
        classRoomRepository.saveAndFlush(class1);
        classRoomRepository.saveAndFlush(class2);
        sub1.setStudents(List.of(student1,student3));
        sub2.setStudents(List.of(student2,student3));
        sub3.setStudents(List.of(student1,student2,student3));
        subjectRepository.saveAndFlush(sub1);
        subjectRepository.saveAndFlush(sub2);
        subjectRepository.saveAndFlush(sub3);

    }
}
