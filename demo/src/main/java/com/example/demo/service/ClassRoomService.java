package com.example.demo.service;

import com.example.demo.entities.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.ClassErrors;
import com.example.demo.enumUsages.StudentErrors;
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
    public ClassErrors joinClass(Long studentId, Long classId) {
        if(!studentRepository.existsById(studentId)){
            return ClassErrors.NotFound_Student;
        }
        Student student = studentRepository.findById(studentId).get();
        if(!classRoomRepository.existsById(classId)){
            return ClassErrors.NotFound_Class;
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        if(student.getClassRoom() != null && student.getClassRoom().equals(classRoom)){
            return ClassErrors.Already_Join;
        }
        if(classRoom.getMaxStudents() == classRoom.getStudents().size()){
            return ClassErrors.Full_Students;
        }

        classRoom.getStudents().add(student);
        classRoom.setStudents(classRoom.getStudents());
        student.setClassRoom(classRoom);
        return null;
    }

    @Transactional
    public ClassErrors updateClassRoom(Long classId, String newName, String newMax, String block) {
        if(!classRoomRepository.existsById(classId)){
            return ClassErrors.NotFound_Class;
        }
        for (ClassRoom c: classRoomRepository.findAll()) {
            if(c.getId() != classId && c.getName().equalsIgnoreCase(newName)){
                return ClassErrors.Name_Exist;
            }
        }
        if(newMax != null && newMax.length()>0){
            try{
                int max = Integer.parseInt(newMax);
                if(max <= 0){
                    return ClassErrors.MaxStudents_Positive;
                }
                ClassRoom classRoom = classRoomRepository.findById(classId).get();
                if(max < classRoom.getStudents().size()){
                    return ClassErrors.MaxStudents_Invalid;
                }
                if(newName != null && newName.length()>0)
                    classRoom.setName(newName);
                classRoom.setMaxStudents(max);
            }catch (NumberFormatException n){
                return ClassErrors.Wrong_Number_Format;
            }
        }
        try{
            Block block1 = Block.valueOf(block);
        }catch (Exception e){
            return ClassErrors.Block_Invalid;
        }
        return null;
    }

    public void deleteClass(Long classId) {
        if(!classRoomRepository.existsById(classId)){
            throw new IllegalStateException();
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        classRoomRepository.deleteById(classId);

    }
}
