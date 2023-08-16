package com.example.demo.securingweb;

import com.example.demo.enumUsages.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long roleId;
    private RoleName authority;

    public Role(RoleName authority){
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.toString();
    }
}
