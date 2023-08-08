package com.example.demo.controller;

import com.example.demo.entities.Student;
import com.example.demo.repository.StudentDTO;
import com.example.demo.service.StudentService;
import com.example.demo.view.Views;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    //    @Autowired
//    public StudentController(StudentService studentService){
//        this.studentService = studentService;
//    }
    @GetMapping("/student")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }
    @GetMapping("/students-by-subject/{subjectId}")
    public ResponseEntity<?> getStudentsBySubjectId(@PathVariable("subjectId") Long subjectId) {
        List<Student> studentList = new ArrayList<>();
        try{
            studentList = studentService.getStudentsBySubjectId(subjectId);
            MappingJacksonValue result = new MappingJacksonValue(studentList);
            result.setSerializationView(Views.StudentWithoutSubject.class);
            return  ResponseEntity.ok(result);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(null) ;
        }

    }

    @GetMapping("/students-by-classroom/{classroomId}")
    public ResponseEntity<?> getStudentsByClassRoomId(@PathVariable("classroomId") Long classRoomId) {
        List<Student> studentList = new ArrayList<>();
        try{
            studentList = studentService.getStudentsByClassRoomId(classRoomId);
            MappingJacksonValue result = new MappingJacksonValue(studentList);
            result.setSerializationView(Views.StudentWithoutClass.class);
            return  ResponseEntity.ok(result);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(null) ;
        }

    }
    @PostMapping("/add-student")
    public ResponseEntity<String> registerNewStudent(@RequestBody StudentDTO studentDTO){
        try{
            studentService.addNewStudent(studentDTO);
        }catch (DateTimeParseException d){
            return ResponseEntity.badRequest().body("Date is invalid");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Email taken");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Add successfully");
    }
    @DeleteMapping("/delete-student/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long id){
        try{
            studentService.deleteStudent(id);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("student with id " + id + " does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");

    }
    @PutMapping("/update-student/{studentId}")
    public ResponseEntity<String> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        try{
            studentService.updateStudent(studentId, name, email);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("student with id " + studentId + " does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update successfully");

    }
}
