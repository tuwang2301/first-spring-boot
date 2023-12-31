package com.example.demo.classroom;

import com.example.demo.classroom.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {
    @Query("SELECT c FROM ClassRoom c where c.name = ?1")
    Optional<ClassRoom> findByName(String name);
}
