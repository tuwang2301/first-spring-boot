package com.example.demo.controller;

import com.example.demo.entities.Student;
import com.example.demo.errorhandler.ClassException;
import com.example.demo.errorhandler.StudentException;
import com.example.demo.errorhandler.SubjectException;
import com.example.demo.pagination.PaginatedResponse;
import com.example.demo.pagination.PaginationMeta;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseObject.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})})
    public ResponseEntity<?> getStudents(
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) String className,
            @RequestParam(required = false) String subjectName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String conduct,
            @RequestParam(required = false) String classBlock,
            @RequestParam(required = false) String subjectBlock,
            @RequestParam(required = true, defaultValue = "1") int currentPage,
            @RequestParam(required = true, defaultValue = "3") int pageSize

    ) {
        try {
            Page<Student> students = studentService.searchStudents(studentName, className, subjectName, gender, rank, conduct, classBlock, subjectBlock, currentPage, pageSize);
            PaginationMeta meta = new PaginationMeta(
                    currentPage,
                    pageSize,
                    (int) students.getTotalElements(),
                    students.getTotalPages()
            );
            MappingJacksonValue result = new MappingJacksonValue(students.getContent());
            result.setSerializationView(Views.StudentWithoutClass.class);
            return ResponseEntity.ok(new ResponseObject<>("sucess", "Search successfully", new PaginatedResponse(meta, result)));
        } catch (StudentException studentException) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail", studentException.getMessage(), null));
        } catch (ClassException classException) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail", classException.getClassErrors().getMessage(), null));
        } catch (SubjectException subjectException) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail", subjectException.getSubjectErrors().getMessage(), null));
        }

    }

//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<?> getStudentDetailById(@PathVariable Long studentId) {
//        Student student = studentService.getStudentById(studentId);
//        if (student.equals(null)) {
//            return ResponseEntity.badRequest().body("Student with id " + studentId + " not found");
//        }
//        return ResponseEntity.ok(student);
//    }

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

//    @GetMapping("/students-by-classroom/{classroomId}")
//    @Operation(summary = "Lấy ra toàn bộ học sinh theo lớp học")
//    public ResponseEntity<?> getStudentsByClassRoomId(@PathVariable("classroomId") Long classRoomId) {
//        List<Student> studentList = new ArrayList<>();
//        try {
//            studentList = studentService.getStudentsByClassRoomId(classRoomId);
//            MappingJacksonValue result = new MappingJacksonValue(studentList);
//            result.setSerializationView(Views.StudentWithoutClass.class);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//    }

    @Operation(summary = "Thêm mới học sinh")
    @PostMapping("/add-student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})})
    public ResponseEntity<?> registerNewStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student s = studentService.addNewStudent(studentDTO);
            return ResponseEntity.ok(new ResponseObject<>("success", "Add student successfully", s));
        } catch (StudentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail", "Add student successfully", null));
        }
    }

//    @PostMapping("/add-student-entity")
//    @Operation(summary = "Thêm mới học sinh bằng entity")
//    public ResponseEntity<?> addStudentByEntity(@RequestBody Student student){
//        try{
//            Student newStudent = studentService.addStudentByEntity(student);
//            return ResponseEntity.ok(new ResponseObject<>("success", "Add student successfully",student));
//        }catch (StudentException studentException){
//            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",studentException.getStudentErrors().getMessage(),null));
//        }
//    }

    @DeleteMapping("/delete-student/{studentId}")
    @Operation(summary = "Xóa môn học")
    public ResponseEntity<?> deleteStudent(@PathVariable("studentId") Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok(new ResponseObject<>("success","Delete successfully",null));
        } catch (StudentException e) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",e.getStudentErrors().getMessage(),"Delete unsuccessfully"));
        }

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
        try {
            Student s = studentService.updateStudent(studentId, studentDTO);
            return ResponseEntity.ok(new ResponseObject<>("succes", "Update student successfully", s));
        } catch (StudentException s) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail", "Update student unsuccessfully", null));
        }
    }
}
