package com.example.demo.classroom;

import com.example.demo.common.ResponseObject;
import com.example.demo.student.Student;
import com.example.demo.student.StudentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "ClassRoom", description = "ClassRoom management API")
@RequestMapping("/classroom")
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping("/classroom")
    @Operation(summary = "Lấy ra tất cả lớp học")
    public List<ClassRoom> getClassRooms() {
        return classRoomService.getClassRooms();
    }

    @PostMapping("/add-classroom")
    @Operation(summary = "Thêm mới lớp học")
    public ResponseEntity<?> addClassRoom(@RequestBody ClassRoomDTO classRoomDTO) {
        try {
            ClassRoom classRoom = classRoomService.addClassRoom(classRoomDTO);
            return ResponseEntity.ok(new ResponseObject<>("success","Add classroom successfully",classRoom));
        }catch (ClassException c){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",c.getClassErrors().getMessage(),null));
        }
    }

    @PutMapping("/join-class")
    @Operation(summary = "Cho học sinh tham gia lớp học")
    public ResponseEntity<?> joinClassRoom(
            @RequestParam Long studentId,
            @RequestParam Long classId
    ) {
        try{
            Student student = classRoomService.joinClass(studentId, classId);
            return ResponseEntity.ok(new ResponseObject<>("success","Join classroom successfully",student));
        }catch (StudentException s){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",s.getStudentErrors().getMessage(),null));
        }catch (ClassException c){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",c.getClassErrors().getMessage(),null));
        }
    }

    @PutMapping("update-classroom/{classId}")
    @Operation(summary = "Cập nhật lớp học")
    public ResponseEntity<?> updateClassRoom(
            @PathVariable Long classId,
            @RequestParam(required = false) String newName,
            @RequestParam(required = false) String newMax,
            @RequestParam(required = false) String block
    ){
        try{
            ClassRoom classRoom = classRoomService.updateClassRoom(classId, newName, newMax, block);
            return ResponseEntity.ok(new ResponseObject<>("success","Update classroom successfully",classRoom));
        }catch (ClassException c){
            return ResponseEntity.badRequest().body(new ResponseObject<>("fail",c.getClassErrors().getMessage(),null));
        }
    }

    @DeleteMapping("delete-classroom/{classId}")
    @Operation(summary = "Xóa lớp học")
    public ResponseEntity<?> deleteClassRoom(@PathVariable Long classId){
        try{
            classRoomService.deleteClass(classId);
            return ResponseEntity.ok(new ResponseObject<>("success","Delete successfully",null));
        }catch (ClassException e){
            return ResponseEntity.badRequest().body(e.getClassErrors().getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There are students attending this class, so you cannot delete");
        }
    }
}
