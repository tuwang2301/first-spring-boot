package com.example.demo.controller;

import com.example.demo.entities.Subject;
import com.example.demo.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/subject")
    public List<Subject> getSubjects(){
        return subjectService.getSubjects();
    }

    @GetMapping("/student-subjects")
    public List<Subject> getSubjectsByStudentId(@RequestParam Long studentId){
        return subjectService.getSubjectsByStudentId(studentId);
    }


    @PostMapping("/add-subject")
    public ResponseEntity<String> addSubject(@RequestBody Subject subject){
        try{
            subjectService.addSubject(subject);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("The name already exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Add successfully");
    }

    @PutMapping("/update-subject/{subjectId}")
    public ResponseEntity<String> updateSubject(
            @PathVariable("subjectId") Long subjectId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer credits
    ){
        int status = subjectService.updateSubject(subjectId, name, credits);
        if(status == 1) {
            return ResponseEntity.badRequest().body("Subject with id " + subjectId + " not found");
        }else if(status == 2) {
            return ResponseEntity.badRequest().body("Subject with name " + name + " already exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Update successfully");
    }

    @DeleteMapping("/delete-subject/{subjectId}")
    public ResponseEntity<String> deleteSubject(@PathVariable Long subjectId){
        try{
            subjectService.deleteSubject(subjectId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Subject with id " + subjectId + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Delete successfully");

    }

    @PutMapping("/register-subjects/{studentId}")
    public ResponseEntity<String> registerSubjects(
            @PathVariable("studentId") Long studentId,
            @RequestBody List<Long> subjectIds
    ){
        int status = subjectService.registerSubjects(studentId, subjectIds);
        if(status == 1) {
            return ResponseEntity.badRequest().body("Student with id " + studentId + " not found");
        }else if(status == 2) {
            return ResponseEntity.badRequest().body("Subject not found");
        }else if(status == 3){
            return ResponseEntity.badRequest().body("Student already register this subject");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Register successfully");
    }

    @PutMapping("/unregister-subject")
    public ResponseEntity<?> unregisterSubjects(
            @RequestParam Long studentId,
            @RequestParam Long subjectId
    ){
        int status = subjectService.unregisterSubject(studentId, subjectId);
        switch (status){
            case 1:
                return ResponseEntity.badRequest().body("Student not exist");
            case 2:
                return ResponseEntity.badRequest().body("Subject not exist");
            case 3:
                return ResponseEntity.badRequest().body("Student not register this subject");
            default:
                return ResponseEntity.ok("Unregister successfully");

        }
    }

}
