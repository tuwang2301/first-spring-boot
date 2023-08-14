package com.example.demo.classroom;

import com.example.demo.classroom.ClassRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO {
    @Schema(required = true, maxLength = 10, example = "B1", pattern = "^[A-Z][0-9]+$")
    private String name;
    @Schema(format = "int64"
            , required = true)
    private String maxStudents;
    @Schema(required = true, maxLength = 4)
    private String classBlock;

    public void loadFromEntity(ClassRoom classRoom) {
        this.name = classRoom.getName();
        this.maxStudents = classRoom.getMaxStudents().toString();
    }
}
