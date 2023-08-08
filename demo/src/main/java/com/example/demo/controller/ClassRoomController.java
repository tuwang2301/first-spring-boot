package com.example.demo.controller;

import com.example.demo.entities.ClassRoom;
import com.example.demo.repository.ClassRoomDTO;
import com.example.demo.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

@RestController
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/classroom")
    public List<ClassRoom> getClassRooms() {
        return classRoomService.getClassRooms();
    }

    @PostMapping("/add-classroom")
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
    public ResponseEntity<?> joinClassRoom(
            @RequestParam Long studentId,
            @RequestParam Long classId
    ) {
        int status = classRoomService.joinClass(studentId, classId);
        switch (status) {
            case 1:
                return ResponseEntity.badRequest().body("Student not exist");
            case 2:
                return ResponseEntity.badRequest().body("Class not exist");
            case 3:
                return ResponseEntity.badRequest().body("This class has full of students");
            case 4:
                return ResponseEntity.badRequest().body("This student already join this class");
            default:
                return ResponseEntity.ok("Join successfully");
        }
    }

    @PutMapping("update-classroom/{classId}")
    public ResponseEntity<?> updateClassRoom(
            @PathVariable Long classId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newMax
    ){
        int status = classRoomService.updateClassRoom(classId, newName, newMax);
        switch (status){
            case 1:
                return ResponseEntity.badRequest().body("Class not exist");
            case 2:
                return ResponseEntity.badRequest().body("Class name already exist");
            case 3:
                return ResponseEntity.badRequest().body("Wrong number format");
            case 4:
                return ResponseEntity.badRequest().body("Max students > 0");
            case 5:
                return ResponseEntity.badRequest().body("Number of students > exist students in class");
            default:
                return ResponseEntity.ok("Update successfuly!");
        }
    }

    @DeleteMapping("delete-classroom/{classId}")
    public ResponseEntity<?> deleteClassRoom(@PathVariable Long classId){
        try{
            classRoomService.deleteClass(classId);
            return ResponseEntity.ok("Delete successfully!");
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("Class with id " + classId + " does not exist");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete class: " + e.getMessage());
        }
    }
}
