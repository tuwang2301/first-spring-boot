    package com.example.demo.student;

    import com.example.demo.enumUsages.Gender;
    import com.example.demo.validate.ValidEnumValue;
    import io.swagger.v3.oas.annotations.media.Schema;
    import jakarta.validation.constraints.Email;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class StudentDTO {
        @Schema(required = true,
                minLength = 2,
                maxLength = 50,
                example = "Nguyen Van A")
        private String name;
        @Schema(format = "date-time"
                , example = "1990-01-01"
                , required = true)
        private String dob;
        @Schema(required = true)
        @Email(message = "Email is invalid")
        private String email;
        @Schema(required = true,
                maxLength = 7)
        @ValidEnumValue(enumClass = Gender.class, allowedValues = {"Male", "Female", "Unknown"})
        private String gender;
        @Schema(required = true,
                maxLength = 9)
        private String rank;
        @Schema(required = true,
                maxLength = 6)
        private String conduct;

        public void loadFromEntity(Student student) {
            this.name = student.getName();
            this.dob = student.getDob().toString();
            this.email = student.getEmail();
        }
    }
