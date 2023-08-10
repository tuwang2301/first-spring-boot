package com.example.demo.controller;

import com.example.demo.entities.ClassRoom;
import com.example.demo.enumUsages.ClassErrors;
import com.example.demo.repository.ClassRoomDTO;
import com.example.demo.service.ClassRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

@RestController
@Tag(name = "ClassRoom", description = "ClassRoom management API")
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/classroom")
    @Operation(summary = "Lấy ra tất cả lớp học")
    public List<ClassRoom> getClassRooms() {
        return classRoomService.getClassRooms();
    }

    @PostMapping("/add-classroom")
    @Operation(summary = "Lấy ra tất cả lớp học")
    public ResponseEntity<?> addClassRoom(@RequestBody ClassRoomDTO classRoomDTO) {
        try {
            classRoomService.addClassRoom(classRoomDTO);
            return ResponseEntity.ok("Add successfully");
        } catch (NumberFormatException n) {
            return ResponseEntity.badRequest().body("Wrong number format");
        } catch (DuplicateFormatFlagsException d) {
            return ResponseEntity.badRequest().body("Class name already exist");
        } catch (IllegalStateException i) {
            return ResponseEntity.badRequest().body("Max students > 0");
        }
    }

    @PutMapping("/join-class")
    @Operation(summary = "Cho học sinh tham gia lớp học")
    public ResponseEntity<?> joinClassRoom(
            @RequestParam Long studentId,
            @RequestParam Long classId
    ) {
        ClassErrors status = classRoomService.joinClass(studentId, classId);
        return switch (status) {
            case NotFound_Student -> ResponseEntity.badRequest().body("Student not exist");
            case NotFound_Class -> ResponseEntity.badRequest().body("Class not exist");
            case Full_Students -> ResponseEntity.badRequest().body("This class has full of students");
            case Name_Exist -> null;
            case Wrong_Number_Format -> null;
            case MaxStudents_Positive -> null;
            case MaxStudents_Invalid -> null;
            case Already_Join -> ResponseEntity.badRequest().body("This student already join this class");
            case null -> ResponseEntity.ok("Join successfully");
            case Block_Invalid -> null;
        };
    }

    @PutMapping("update-classroom/{classId}")
    @Operation(summary = "Cập nhật lớp học")
    public ResponseEntity<?> updateClassRoom(
            @PathVariable Long classId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newMax,
            @RequestParam(required = false) String block
    ){
        ClassErrors status = classRoomService.updateClassRoom(classId, newName, newMax, block);
        return switch (status) {
            case NotFound_Student -> null;
            case NotFound_Class -> ResponseEntity.badRequest().body("Class not exist");
            case Name_Exist -> ResponseEntity.badRequest().body("Class name already exist");
            case Wrong_Number_Format -> ResponseEntity.badRequest().body("Wrong number format");
            case MaxStudents_Positive -> ResponseEntity.badRequest().body("Max students > 0");
            case MaxStudents_Invalid -> ResponseEntity.badRequest().body("Number of students > exist students in class");
            case Already_Join -> null;
            case Full_Students -> null;
            case Block_Invalid -> ResponseEntity.badRequest().body("Block is invalid");
            case null -> ResponseEntity.ok("Update successfuly!");
        };
    }

    @DeleteMapping("delete-classroom/{classId}")
    @Operation(summary = "Xóa lớp học")
    public ResponseEntity<?> deleteClassRoom(@PathVariable Long classId){
        try{
            classRoomService.deleteClass(classId);
            return ResponseEntity.ok("Delete successfully!");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("Class with id " + classId + " does not exist");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There are students attending this class, so you cannot delete");
        }
    }
}
