package com.example.demo.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClassRoomController {
    @Autowired
    private ClassRoomService classRoomService;
    @GetMapping("/class-room")
    public List<ClassRoom> getClassRooms(){
        return classRoomService.getClassRooms();
    }
}
