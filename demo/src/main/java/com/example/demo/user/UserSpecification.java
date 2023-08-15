package com.example.demo.user;

import com.example.demo.securingweb.ApplicationUser_;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<UserProjection> hasUserName(String userName){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ApplicationUser_.USERNAME),userName);
        };
    }

}
