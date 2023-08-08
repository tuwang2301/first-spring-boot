package com.example.demo.service;

import com.example.demo.entities.Student;
import com.example.demo.repository.ClassRoomDTO;
import com.example.demo.repository.ClassRoomRepository;
import com.example.demo.entities.ClassRoom;
import com.example.demo.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.attoparser.ParseException;
import org.springframework.stereotype.Service;

import java.util.DuplicateFormatFlagsException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ClassRoomService {
    private final ClassRoomRepository classRoomRepository;
    private final StudentRepository studentRepository;
    public List<ClassRoom> getClassRooms() {
        return classRoomRepository.findAll();
    }

    public void addClassRoom(ClassRoomDTO classRoomDTO) {
        if(classRoomRepository.findByName(classRoomDTO.getName()).isPresent()){
            throw new DuplicateFormatFlagsException("");
        }
        try{
            int max = Integer.parseInt(classRoomDTO.getMaxStudents());
            if(max <= 0){
                throw new IllegalStateException();
            }
            ClassRoom classRoom = new ClassRoom();
            classRoom.loadFromDto(classRoomDTO);
            classRoomRepository.save(classRoom);
        }catch (NumberFormatException n){
            throw n;
        }
    }

    @Transactional
    public int joinClass(Long studentId, Long classId) {
        if(!studentRepository.existsById(studentId)){
            return 1;
        }
        Student student = studentRepository.findById(studentId).get();
        if(!classRoomRepository.existsById(classId)){
            return 2;
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        if(student.getClassRoom().equals(classRoom)){
            return 4;
        }
        if(classRoom.getMaxStudents() == classRoom.getStudents().size()){
            return 3;
        }

        classRoom.getStudents().add(student);
        classRoom.setStudents(classRoom.getStudents());
        student.setClassRoom(classRoom);
        return 0;
    }

    @Transactional
    public int updateClassRoom(Long classId, String newName, String newMax) {
        if(!classRoomRepository.existsById(classId)){
            return 1;
        }
        for (ClassRoom c: classRoomRepository.findAll()) {
            if(c.getId() != classId && c.getName().equalsIgnoreCase(newName)){
                return 2;
            }
        }
        if(newMax != null && newMax.length()>0){
            try{
                int max = Integer.parseInt(newMax);
                if(max <= 0){
                    return 4;
                }
                ClassRoom classRoom = classRoomRepository.findById(classId).get();
                if(max < classRoom.getStudents().size()){
                    return 5;
                }
                if(newName != null && newName.length()>0)
                    classRoom.setName(newName);
                classRoom.setMaxStudents(max);
                return 0;
            }catch (NumberFormatException n){
                return 3;
            }
        }
        return 3;
    }

    public void deleteClass(Long classId) {
        if(!classRoomRepository.existsById(classId)){
            throw new IllegalStateException();
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        classRoom.getStudents().clear();
        classRoomRepository.deleteById(classId);

    }
}
