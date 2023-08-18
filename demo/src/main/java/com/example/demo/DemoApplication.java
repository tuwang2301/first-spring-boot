package com.example.demo;

import com.example.demo.classroom.ClassRoom;
import com.example.demo.enumUsages.*;
import com.example.demo.classroom.ClassRoomRepository;
import com.example.demo.user.ApplicationUser;
import com.example.demo.securingweb.Role;
import com.example.demo.securingweb.RoleRepository;
import com.example.demo.user.UserRepository;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.subject.Subject;
import com.example.demo.subject.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        ClassRoom class1 = ClassRoom.builder()
//                .name("A1")
//                .maxStudents(30)
//                .classBlock(Block.KHTN)
//                .build();
//        ClassRoom class2 = ClassRoom.builder()
//                .name("D1")
//                .maxStudents(25)
//                .classBlock(Block.KHXH)
//                .build();
//
//        Subject sub1 = Subject.builder()
//                .name("Math")
//                .credits(4)
//                .startTime(LocalDate.of(2023, Month.JANUARY, 01))
//                .endTime(LocalDate.of(2023, Month.DECEMBER, 01))
//                .subjectBlock(Block.KHTN)
//                .build();
//        Subject sub2 = Subject.builder()
//                .name("English")
//                .credits(2)
//                .startTime(LocalDate.of(2023, Month.SEPTEMBER, 01))
//                .endTime(LocalDate.of(2024, Month.SEPTEMBER, 01))
//                .subjectBlock(Block.KHXH)
//                .build();
//        Subject sub3 = Subject.builder()
//                .name("Programming")
//                .credits(3)
//                .startTime(LocalDate.of(2023, Month.SEPTEMBER, 01))
//                .endTime(LocalDate.of(2024, Month.SEPTEMBER, 01))
//                .subjectBlock(Block.KHTN)
//                .build();
//        Subject sub4 = Subject.builder()
//                .name("Biology")
//                .credits(2)
//                .startTime(LocalDate.of(2022, Month.JANUARY, 01))
//                .endTime(LocalDate.of(2023, Month.JANUARY, 01))
//                .subjectBlock(Block.KHTN)
//                .build();
//        Subject sub5 = Subject.builder()
//                .name("Geography")
//                .credits(2)
//                .startTime(LocalDate.of(2023, Month.AUGUST, 05))
//                .endTime(LocalDate.of(2024, Month.AUGUST, 05))
//                .subjectBlock(Block.KHXH)
//                .build();
//        Subject sub6 = Subject.builder()
//                .name("Chemistry")
//                .credits(3)
//                .startTime(LocalDate.of(2023, Month.AUGUST, 05))
//                .endTime(LocalDate.of(2024, Month.AUGUST, 05))
//                .subjectBlock(Block.KHTN)
//                .build();
//        Subject sub7 = Subject.builder()
//                .name("Physics")
//                .credits(3)
//                .startTime(LocalDate.of(2023, Month.AUGUST, 05))
//                .endTime(LocalDate.of(2024, Month.AUGUST, 05))
//                .subjectBlock(Block.KHTN)
//                .build();
//        Subject sub8 = Subject.builder()
//                .name("Literature")
//                .credits(2)
//                .startTime(LocalDate.of(2023, Month.AUGUST, 05))
//                .endTime(LocalDate.of(2024, Month.AUGUST, 05))
//                .subjectBlock(Block.KHXH)
//                .build();
//        Subject sub9 = Subject.builder()
//                .name("Ethics")
//                .credits(4)
//                .startTime(LocalDate.of(2023, Month.AUGUST, 05))
//                .endTime(LocalDate.of(2024, Month.AUGUST, 05))
//                .subjectBlock(Block.KHXH)
//                .build();
//
//        Student student1 = Student.builder()
//                .name("Nguyen Quang Tu")
//                .dob(LocalDate.of(2003, Month.JANUARY, 23))
//                .email("quangtu2301@gmail.com")
//                .gender(Gender.Male)
//                .rank(Rank.Excellent)
//                .conduct(Conduct.Good)
//                .classRoom(class1)
//                .build();
//        Student student2 = Student.builder()
//                .name("Nguyen Ha Linh")
//                .dob(LocalDate.of(2000, Month.MAY, 23))
//                .email("halinh2305@gmail.com")
//                .gender(Gender.Female)
//                .rank(Rank.Good)
//                .conduct(Conduct.Good)
//                .classRoom(class1)
//                .build();
//        Student student3 = Student.builder()
//                .name("Nguyen Quang Anh")
//                .dob(LocalDate.of(2004, Month.DECEMBER, 2))
//                .email("quanganh0212@gmail.com")
//                .gender(Gender.Unknown)
//                .rank(Rank.Medium)
//                .conduct(Conduct.Bad)
//                .classRoom(class2)
//                .build();
//        List<Student> studentsD1 = new ArrayList<>();
//        studentsD1.add(student3);
//        for (int i = 0; i < 24; i++) {
//            Student s = Student.builder()
//                    .name("Nguyen Quang Anh " + i)
//                    .dob(LocalDate.of(2004, Month.DECEMBER, 2))
//                    .email("quanganh0212"+i+"@gmail.com")
//                    .gender(Gender.Unknown)
//                    .rank(Rank.Medium)
//                    .conduct(Conduct.Bad)
//                    .classRoom(class2)
//                    .build();
//            studentsD1.add(s);
//        }
//        List<Student> studentsA1 = new ArrayList<>();
//        studentsA1.add(student1);
//        studentsA1.add(student2);
//        for (int i = 0; i < 28; i++) {
//            Student s = Student.builder()
//                    .name("Nguyen Van A " + i)
//                    .dob(LocalDate.of(2004, Month.DECEMBER, 2))
//                    .email("nguyenvana0212"+i+"@gmail.com")
//                    .gender(Gender.Unknown)
//                    .rank(Rank.Medium)
//                    .conduct(Conduct.Bad)
//                    .classRoom(class2)
//                    .build();
//            studentsA1.add(s);
//        }
////        Teacher teacher1 = Teacher.builder()
////                .name("Co giao A")
////                .dob(LocalDate.of(1990, Month.JANUARY, 1))
////                .email("cogiaoa@gmail")
////                .build();
//
//        class1.setStudents(studentsA1);
//        class2.setStudents(studentsD1);
//        classRoomRepository.saveAllAndFlush(List.of(class1, class2));
//        sub1.setStudents(List.of(student1,student3));
//        sub2.setStudents(List.of(student2,student3));
//        sub3.setStudents(List.of(student1,student2,student3));
//        subjectRepository.saveAllAndFlush(List.of(sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8,sub9));
//        student1.setSubjects(List.of(sub1, sub3));
//        student2.setSubjects(List.of(sub2, sub3));
//        student3.setSubjects(List.of(sub1, sub2, sub3));
//
//        Role adminRole = roleRepository.save(new Role(RoleName.ADMIN));
//        Role studentRole = roleRepository.save(new Role(RoleName.STUDENT));
//        Role teacherRole = roleRepository.save(new Role(RoleName.TEACHER));
//
//        Set<Role> roles1 = new HashSet<>();
//        roles1.add(adminRole);
//        Set<Role> roles2 = new HashSet<>();
//        roles2.add(studentRole);
//        Set<Role> roles3 = new HashSet<>();
//        roles3.add(adminRole);
//        roles3.add(teacherRole);
//
//        ApplicationUser admin = new ApplicationUser(1L, "admin", passwordEncoder.encode("password"),roles1);
//        ApplicationUser student = new ApplicationUser(2L, "quangtu",passwordEncoder.encode("password"),roles2);
//        ApplicationUser teacher = new ApplicationUser(3L, "superteacher",passwordEncoder.encode("password"),roles3);
//        userRepository.saveAllAndFlush(List.of(admin,student,teacher));
    }
}
