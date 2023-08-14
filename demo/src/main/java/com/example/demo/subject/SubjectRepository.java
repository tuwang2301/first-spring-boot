package com.example.demo.subject;

import com.example.demo.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long>, JpaSpecificationExecutor<Subject> {
    @Query("SELECT s FROM Subject s WHERE s.name = ?1")
    Optional<Subject> findByName(String name);
}
