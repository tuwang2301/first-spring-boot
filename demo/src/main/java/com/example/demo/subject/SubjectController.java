package com.example.demo.subject;

import com.example.demo.student.Student;
import com.example.demo.common.ResponseObject;
import com.example.demo.student.StudentException;
import com.example.demo.pagination.PaginatedResponse;
import com.example.demo.pagination.PaginationMeta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Subject", description = "Subject management API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subjects")
    @Operation(summary = "Lấy ra toàn bộ môn học")
    @RolesAllowed({"ADMIN","STUDENT","TEACHER"})
    public ResponseEntity<?> getSubjects(
            @RequestParam(required = false) String subjectBlock,
            @RequestParam(required = false) String credits,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = true, defaultValue = "1") int currentPage,
            @RequestParam(required = true, defaultValue = "3") int pageSize
    ) {
        Page<Subject> subjectPage = null;
        try {
            subjectPage = subjectService.searchSubjects(subjectBlock, credits, startTime, endTime, studentName, currentPage, pageSize);
            PaginationMeta paginationMeta = new PaginationMeta(
                    currentPage,
                    pageSize,
                    (int) subjectPage.getTotalElements(),
                    subjectPage.getTotalPages()
            );
            return ResponseEntity.ok(new ResponseObject<>("success","Search successfully",new PaginatedResponse<>(paginationMeta, subjectPage)));
        } catch (SubjectException subjectException) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",subjectException.getSubjectErrors().getMessage(), null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }

    }

    @GetMapping("/subjects-by-student/{studentId}")
    @Operation(summary = "Lấy ra toàn bộ môn học của một học sinh")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> getSubjectsByStudentId(@PathVariable Long studentId) {
        try{
            List<Subject> subjects = subjectService.getSubjectsByStudentId(studentId);
            return ResponseEntity.ok(new ResponseObject<>("success","Get successfully",subjects));
        }catch (StudentException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getStudentErrors().getMessage(), "Get unsuccessfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }
    }


    @PostMapping("/add-subject")
    @Operation(summary = "Thêm mới môn học")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class))})
            , @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = String.class))})
    })
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> addSubject(@RequestBody SubjectDTO subjectDTO) {
        try{
            Subject newSubject = subjectService.addSubject(subjectDTO);
            return ResponseEntity.ok(new ResponseObject<>("success","Add successfully", newSubject));
        }catch (SubjectException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getSubjectErrors().getMessage(),"Add unsuccessfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }
    }

    @PutMapping("/update-subject/{subjectId}")
    @Operation(summary = "Cập nhật môn học")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> updateSubject(
            @PathVariable("subjectId") Long subjectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String credits,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String subjectBlock

    ) {
        SubjectDTO subjectDTO = new SubjectDTO(name, credits, startTime, endTime, subjectBlock);
        try{
            Subject subject = subjectService.updateSubject(subjectId, subjectDTO);
            return ResponseEntity.ok(new ResponseObject<>("success","Update successfully",subject));
        }catch (SubjectException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getSubjectErrors().getMessage(),"Update unsuccessfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }
    }

    @DeleteMapping("/delete-subject/{subjectId}")
    @Operation(summary = "Xóa môn học")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<?> deleteSubject(@PathVariable Long subjectId) {
        try {
            subjectService.deleteSubject(subjectId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject<>("success","Delete successfully",null));
        } catch (SubjectException e) {
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",e.getSubjectErrors().getMessage(),"Delete unsuccessfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There are students register this subject, so you cannot delete");
        }


    }

    @PutMapping("/register-subject")
    @Operation(summary = "Đăng kí môn học")
    @RolesAllowed({"ADMIN","STUDENT"})
    public ResponseEntity<?> registerSubjects(
            @RequestParam Long studentId,
            @RequestParam Long subjectId
    ) {
        try{
            Student student = subjectService.registerSubjects(studentId, subjectId);
            return ResponseEntity.ok(new ResponseObject<>("success","Register successfully",student));
        }catch (SubjectException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getSubjectErrors().getMessage(), "Register unsuccessfully"));
        }catch (StudentException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getStudentErrors().getMessage(), "Register unsuccessfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }
    }

    @PutMapping("/unregister-subject")
    @Operation(summary = "Hủy đăng kí môn học")
    @RolesAllowed({"ADMIN","STUDENT"})
    public ResponseEntity<?> unregisterSubjects(
            @RequestParam Long studentId,
            @RequestParam Long subjectId
    ) {
        try{
            Student student = subjectService.unregisterSubject(studentId, subjectId);
            return ResponseEntity.ok(new ResponseObject<>("success","Unregister successfully", student));
        }catch (SubjectException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getSubjectErrors().getMessage(), "Unregister unsuccessfully"));
        }catch (StudentException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getStudentErrors().getMessage(), "Unregister unsuccessfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseObject<>("fail",e.getMessage(),null));
        }
    }

}
