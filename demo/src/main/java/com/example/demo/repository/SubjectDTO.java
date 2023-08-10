package com.example.demo.repository;

import com.example.demo.enumUsages.Block;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {
    @Schema(required = true)
    private String name;
    @Schema(oneOf = Block.class, required = true)
    private String classBlock;
    @Schema(required = true)
    private String credits;
    @Schema(format = "date-time"
            ,example = "1990-01-01",
            required = true)
    private String startTime;
    @Schema(format = "date-time"
            ,example = "1990-01-01",
            required = true)
    private String endTime;

}
