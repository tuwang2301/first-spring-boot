package com.example.demo.classroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ClassRoomService {
    private final ClassRoomRepository classRoomRepository;
    public List<ClassRoom> getClassRooms() {
        return classRoomRepository.findAll();
    }
}
