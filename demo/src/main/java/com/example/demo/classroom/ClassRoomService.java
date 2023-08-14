package com.example.demo.classroom;

import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.student.StudentErrors;
import com.example.demo.student.StudentException;
import com.example.demo.student.StudentRepository;
import com.example.demo.validate.Validate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassRoomService {
    private final ClassRoomRepository classRoomRepository;
    private final StudentRepository studentRepository;

    public List<ClassRoom> getClassRooms() {
        return classRoomRepository.findAll();
    }

    public ClassRoom addClassRoom(ClassRoomDTO classRoomDTO) {
        if (classRoomRepository.findByName(classRoomDTO.getName()).isPresent()) {
            throw new ClassException(ClassErrors.Name_Exist);
        }
        try {
            int max = Integer.parseInt(classRoomDTO.getMaxStudents());
            if (max <= 0) {
                throw new ClassException(ClassErrors.MaxStudents_Negative);
            }
            ClassRoom classRoom = new ClassRoom();
            classRoom.loadFromDto(classRoomDTO);
            classRoomRepository.save(classRoom);
            return classRoom;
        } catch (NumberFormatException n) {
            throw new ClassException(ClassErrors.Wrong_Number_Format);
        }
    }

    @Transactional
    public Student joinClass(Long studentId, Long classId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentException(StudentErrors.NotFound_Student);
        }
        Student student = studentRepository.findById(studentId).get();
        if (!classRoomRepository.existsById(classId)) {
            throw new ClassException(ClassErrors.NotFound_Class);
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        if (student.getClassRoom() != null && student.getClassRoom().equals(classRoom)) {
            throw new ClassException(ClassErrors.Already_Join);
        }
        if (classRoom.getMaxStudents() == classRoom.getStudents().size()) {
            throw new ClassException(ClassErrors.Full_Students);
        }

        classRoom.getStudents().add(student);
        classRoom.setStudents(classRoom.getStudents());
        student.setClassRoom(classRoom);
        return student;
    }

    @Transactional
    public ClassRoom updateClassRoom(Long classId, String newName, String newMax, String block) {
        if (!classRoomRepository.existsById(classId)) {
            throw new ClassException(ClassErrors.NotFound_Class);
        }
        for (ClassRoom c : classRoomRepository.findAll()) {
            if (c.getId() != classId && c.getName().equalsIgnoreCase(newName)) {
                throw new ClassException(ClassErrors.Name_Exist);
            }
        }
        if (newMax != null && newMax.length() > 0) {
            try {
                int max = Integer.parseInt(newMax);
                if (max <= 0) {
                    throw new ClassException(ClassErrors.MaxStudents_Negative);
                }
                ClassRoom classRoom = classRoomRepository.findById(classId).get();
                if (max < classRoom.getStudents().size()) {
                    throw new ClassException(ClassErrors.MaxStudents_Invalid);
                }
                if (newName != null && newName.length() > 0)
                    classRoom.setName(newName);
                classRoom.setMaxStudents(max);
                Block block1 = Validate.validateBlock(block, ClassRoom.class);
                classRoom.setClassBlock(block1);
                return classRoom;
            } catch (NumberFormatException n) {
                throw new ClassException(ClassErrors.Wrong_Number_Format);
            }
        }
        return null;
    }

    public void deleteClass(Long classId) {
        if (!classRoomRepository.existsById(classId)) {
            throw new ClassException(ClassErrors.NotFound_Class);
        }
        ClassRoom classRoom = classRoomRepository.findById(classId).get();
        classRoomRepository.deleteById(classId);

    }
}
