package com.example.demo.repository;

import com.example.demo.entities.ClassRoom;
import com.example.demo.enumUsages.Block;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomDTO {
    @Schema(required = true)
    private String name;
    @Schema(format = "int64"
            , required = true)
    private String maxStudents;
    @Schema(oneOf = Block.class, required = true)
    private String classBlock;

    public void loadFromEntity(ClassRoom classRoom) {
        this.name = classRoom.getName();
        this.maxStudents = classRoom.getMaxStudents().toString();
    }
}
