package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
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
    @PostMapping("/add-student")
    public ResponseEntity<String> registerNewStudent(@RequestBody Student student){
        try{
            studentService.addNewStudent(student);
        }catch (Exception e){
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
        }catch (Exception e){
            return ResponseEntity.badRequest().body("student with id " + studentId + " does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");

    }
}
