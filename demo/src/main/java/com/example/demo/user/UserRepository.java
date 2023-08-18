package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser,Long>, JpaSpecificationExecutor<UserProjection> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<ApplicationUser> findByUsername(String username);
}
