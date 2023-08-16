package com.example.demo.user;

import com.example.demo.enumUsages.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationUserDTO {
    private String username;
    private String password;
    private Set<String> roles;
}
