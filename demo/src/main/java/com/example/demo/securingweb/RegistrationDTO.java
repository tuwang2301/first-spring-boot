package com.example.demo.securingweb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDTO {
    @Schema(
            defaultValue = "admin"
    )
    private String username;
    @Schema(
            defaultValue = "password"
    )
    private String password;

    public String toString(){
        return "Registration info: username: " + this.username + " password: " + this.password;
    }
}
