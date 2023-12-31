package com.example.demo.subject;

import com.example.demo.enumUsages.Block;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Subject name is required")
    private String name;
    @Schema(required = true, maxLength = 4)
    private String subjectBlock;
    @Schema(required = true, format = "int64")
    private String credits;
    @Schema(format = "date-time"
            , example = "1990-01-01",
            required = true)
    private String startTime;
    @Schema(format = "date-time"
            , example = "1990-01-01",
            required = true)
    private String endTime;

}
