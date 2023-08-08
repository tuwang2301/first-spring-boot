package com.example.demo.repository;

import com.example.demo.entities.ClassRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO {
    private String name;
    private String maxStudents;

    public void loadFromEntity(ClassRoom classRoom){
        this.name = classRoom.getName();
        this.maxStudents = classRoom.getMaxStudents().toString();
    }
}
