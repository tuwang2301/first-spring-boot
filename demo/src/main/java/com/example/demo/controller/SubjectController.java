package com.example.demo.controller;

import com.example.demo.entities.Subject;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.SubjectErrors;
import com.example.demo.repository.SubjectDTO;
import com.example.demo.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subject", description = "Subject management API")
@RestController
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subject")
    @Operation(summary = "Lấy ra toàn bộ môn học")
    public List<Subject> getSubjects() {
        return subjectService.getSubjects();
    }

    @GetMapping("/subjects-by-student/{studentId}")
    @Operation(summary = "Lấy ra toàn bộ môn học của một học sinh")
    public List<Subject> getSubjectsByStudentId(@PathVariable Long studentId) {
        return subjectService.getSubjectsByStudentId(studentId);
    }


    @PostMapping("/add-subject")
    @Operation(summary = "Thêm mới môn học")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class))})
            , @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})
    })
    public ResponseEntity<String> addSubject(@RequestBody SubjectDTO subjectDTO) {
        SubjectErrors status = subjectService.addSubject(subjectDTO);
        return switch (status) {
            case NotFound_Subject -> null;
            case NotFound_Student -> null;
            case Date_Invalid -> ResponseEntity.badRequest().body("Date-time invalid");
            case StartTime_Invalid -> ResponseEntity.badRequest().body("Start time must be after today");
            case EndTime_Invalid ->
                    ResponseEntity.badRequest().body("End time must be after start-time at least one month");
            case Credits_Invalid -> ResponseEntity.badRequest().body("Credits must be integer");
            case Block_Invalid -> ResponseEntity.badRequest().body("Block invalid. Block must be one of " + Block.values().toString());
            case Name_Exist -> ResponseEntity.badRequest().body("Class name already exist");
            case Already_Register -> null;
            case No_Register -> null;
            case Over_Subject -> ResponseEntity.badRequest().body("This subject is over");
            case Started_Subject -> ResponseEntity.badRequest().body("This subject started so you cannot edit");
            case Late_Register ->
                    ResponseEntity.badRequest().body("This subject starts more than a month so you cannot register now");
            case null -> ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        };
    }

    @PutMapping("/update-subject/{subjectId}")
    @Operation(summary = "Cập nhật môn học")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<String> updateSubject(
            @PathVariable("subjectId") Long subjectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String credits,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String subjectBlock

    ) {
        SubjectDTO subjectDTO = new SubjectDTO(name, credits, startTime, endTime, subjectBlock);
        SubjectErrors status = subjectService.updateSubject(subjectId, subjectDTO);
        return switch (status) {
            case NotFound_Subject -> ResponseEntity.badRequest().body("Subject with id " + subjectId + " not found");
            case NotFound_Student -> null;
            case Date_Invalid -> ResponseEntity.badRequest().body("Date-time invalid");
            case StartTime_Invalid -> ResponseEntity.badRequest().body("Start time must be after today");
            case EndTime_Invalid ->
                    ResponseEntity.badRequest().body("End time must be after start-time at least one month");
            case Credits_Invalid -> ResponseEntity.badRequest().body("Credits must be integer");
            case Block_Invalid -> ResponseEntity.badRequest().body("Block invalid. Block must be one of " + Block.values().toString());
            case Name_Exist -> null;
            case Already_Register -> null;
            case No_Register -> null;
            case Over_Subject -> ResponseEntity.badRequest().body("This subject is over");
            case Started_Subject -> ResponseEntity.badRequest().body("This subject started so you cannot edit");
            case Late_Register ->
                    ResponseEntity.badRequest().body("This subject starts more than a month so you cannot register now");
            case null -> ResponseEntity.status(HttpStatus.OK).body("Update successfully");
        };
    }

    @DeleteMapping("/delete-subject/{subjectId}")
    @Operation(summary = "Xóa môn học")
    public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId) {
        try {
            subjectService.deleteSubject(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Subject with id " + subjectId + " does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There are students register this subject, so you cannot delete");
        }


    }

    @PutMapping("/register-subject")
    @Operation(summary = "Đăng kí môn học")
    public ResponseEntity<String> registerSubjects(
            @RequestParam Long studentId,
            @RequestParam Long subjectId
    ) {
        SubjectErrors status = subjectService.registerSubjects(studentId, subjectId);
        return switch (status) {
            case NotFound_Subject -> ResponseEntity.badRequest().body("Subject with id " + subjectId + " not found");
            case NotFound_Student -> ResponseEntity.badRequest().body("Student with id " + studentId + " not found");
            case Date_Invalid -> null;
            case StartTime_Invalid -> null;
            case EndTime_Invalid -> null;
            case Credits_Invalid -> null;
            case Block_Invalid -> null;
            case Name_Exist -> null;
            case Already_Register ->
                    ResponseEntity.badRequest().body("Student with id " + studentId + " already register this subject");
            case No_Register -> null;
            case Over_Subject -> ResponseEntity.badRequest().body("This subject is over");
            case Started_Subject -> null;
            case Late_Register ->
                    ResponseEntity.badRequest().body("This subject starts more than a month so you cannot register now");
            case null -> ResponseEntity.status(HttpStatus.OK).body("Register successfully");
        };
    }

    @PutMapping("/unregister-subject")
    @Operation(summary = "Hủy đăng kí môn học")
    public ResponseEntity<?> unregisterSubjects(
            @RequestParam Long studentId,
            @RequestParam Long subjectId
    ) {
        SubjectErrors status = subjectService.unregisterSubject(studentId, subjectId);
        return switch (status) {
            case NotFound_Subject -> ResponseEntity.badRequest().body("Subject with id " + subjectId + " not found");
            case NotFound_Student -> ResponseEntity.badRequest().body("Student with id " + studentId + " not found");
            case Date_Invalid -> null;
            case StartTime_Invalid -> null;
            case EndTime_Invalid -> null;
            case Credits_Invalid -> null;
            case Block_Invalid -> null;
            case Name_Exist -> null;
            case Already_Register ->
                    ResponseEntity.badRequest().body("Student with id " + studentId + " already register this subject");
            case No_Register ->
                    ResponseEntity.badRequest().body("Student with id " + studentId + " has not register this subject");
            case Over_Subject -> ResponseEntity.badRequest().body("This subject is over");
            case Started_Subject -> ResponseEntity.badRequest().body("This subject started so you cannot unregister.");
            case Late_Register ->
                    ResponseEntity.badRequest().body("This subject starts more than a month so you cannot register now");
            case null -> ResponseEntity.status(HttpStatus.OK).body("Unregister successfully");
        };
    }

}
