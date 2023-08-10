package com.example.demo.controller;

import com.example.demo.entities.Student;
import com.example.demo.enumUsages.*;
import com.example.demo.repository.StudentDTO;
import com.example.demo.service.StudentService;
import com.example.demo.view.Views;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Tag(name = "Student", description = "Student management API")
public class StudentController {
    @Autowired
    private StudentService studentService;

    //    @Autowired
//    public StudentController(StudentService studentService){
//        this.studentService = studentService;
//    }
    @Operation(summary = "Lấy ra tất cả học sinh")
    @GetMapping("/students")
    public ResponseEntity<?> getStudents(
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Rank rank,
            @RequestParam(required = false) Conduct conduct,
            @RequestParam(required = false) Block classBlock,
            @RequestParam(required = false) Block subjectBlock

            ) {
        List<Student> students = studentService.searchStudents(className, subjectName, gender, rank, conduct, classBlock, subjectBlock);
        MappingJacksonValue result = new MappingJacksonValue(students);
        result.setSerializationView(Views.StudentWithoutClass.class);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentDetailById(@PathVariable Long studentId){
        Student student = studentService.getStudentById(studentId);
        if(student.equals(null)){
            return ResponseEntity.badRequest().body("Student with id " + studentId + " not found");
        }
        return ResponseEntity.ok(student);
    }

//    @GetMapping("/students")
//    public ResponseEntity<?> getStudentsBySubjectId(@PathVariable("subjectId") Long subjectId) {
//        List<Student> studentList = new ArrayList<>();
//        try {
//            studentList = studentService.getStudentsBySubjectId(subjectId);
//            MappingJacksonValue result = new MappingJacksonValue(studentList);
//            result.setSerializationView(Views.StudentWithoutSubject.class);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Empty");
//        }
//
//    }

    @GetMapping("/students-by-classroom/{classroomId}")
    @Operation(summary = "Lấy ra toàn bộ học sinh theo lớp học")
    public ResponseEntity<?> getStudentsByClassRoomId(@PathVariable("classroomId") Long classRoomId) {
        List<Student> studentList = new ArrayList<>();
        try {
            studentList = studentService.getStudentsByClassRoomId(classRoomId);
            MappingJacksonValue result = new MappingJacksonValue(studentList);
            result.setSerializationView(Views.StudentWithoutClass.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }

    @Operation(summary = "Thêm mới học sinh")
    @PostMapping("/add-student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})})
    public ResponseEntity<?> registerNewStudent(@RequestBody StudentDTO studentDTO) {
        StudentErrors s = studentService.addNewStudent(studentDTO);
        return switch (s) {
            case NotFound_Student ->
                    ResponseEntity.badRequest().body("student with id    does not exist");
            case Email_Taken ->
                    ResponseEntity.badRequest().body("student with email " + studentDTO.getEmail() + " already exists");
            case Date_Invalid -> ResponseEntity.badRequest().body("Dob " + studentDTO.getDob() + " is invalid");
            case Gender_Invalid ->
                    ResponseEntity.badRequest().body("Gender " + studentDTO.getGender() + " is invalid. Gender has to be one of " + Arrays.toString(Gender.values()));
            case Rank_Invalid ->
                    ResponseEntity.badRequest().body("Rank " + studentDTO.getRank() + " is invalid. Rank has to be one of " + Arrays.toString(Rank.values()));
            case Conduct_Invalid ->
                    ResponseEntity.badRequest().body("Conduct " + studentDTO.getConduct() + " is invalid. Conduct has to be one of " + Arrays.toString(Conduct.values()));
            case null -> ResponseEntity.status(HttpStatus.OK).body("Add successfully");
        };
    }

    @DeleteMapping("/delete-student/{studentId}")
    @Operation(summary = "Xóa môn học")
    public ResponseEntity<String> deleteStudent(@PathVariable("studentId") Long id) {
        try {
            studentService.deleteStudent(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("student with id " + id + " does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");

    }

    @PutMapping("/update-student/{studentId}")
    @Operation(summary = "Cập nhật học sinh")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<?> updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String dob,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String conduct
    ) {
        StudentDTO studentDTO = new StudentDTO(name, dob, email, gender, rank, conduct);
        StudentErrors s = studentService.updateStudent(studentId, studentDTO);
        return switch (s) {
            case NotFound_Student ->
                    ResponseEntity.badRequest().body("student with id " + studentId + " does not exist");
            case Email_Taken ->
                    ResponseEntity.badRequest().body("student with email " + studentDTO.getEmail() + " already exists");
            case Date_Invalid -> ResponseEntity.badRequest().body("Dob " + studentDTO.getDob() + " is invalid");
            case Gender_Invalid ->
                    ResponseEntity.badRequest().body("Gender " + studentDTO.getGender() + " is invalid. Gender has to be one of " + Arrays.toString(Gender.values()));
            case Rank_Invalid ->
                    ResponseEntity.badRequest().body("Rank " + studentDTO.getRank() + " is invalid. Rank has to be one of " + Arrays.toString(Rank.values()));
            case Conduct_Invalid ->
                    ResponseEntity.badRequest().body("Conduct " + studentDTO.getConduct() + " is invalid. Conduct has to be one of " + Arrays.toString(Conduct.values()));
            case null -> ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        };
    }
}
