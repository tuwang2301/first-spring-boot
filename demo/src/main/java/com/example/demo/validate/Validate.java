package com.example.demo.validate;

import com.example.demo.student.StudentErrors;
import com.example.demo.student.StudentException;
import com.example.demo.classroom.ClassErrors;
import com.example.demo.classroom.ClassException;
import com.example.demo.classroom.ClassRoom;
import com.example.demo.student.Student;
import com.example.demo.enumUsages.Block;
import com.example.demo.enumUsages.Conduct;
import com.example.demo.enumUsages.Gender;
import com.example.demo.enumUsages.Rank;
import com.example.demo.subject.SubjectErrors;
import com.example.demo.subject.SubjectException;

import java.time.LocalDate;

public class Validate {
    public static Gender validateGender(String gender) {
        try {
            Gender g = Gender.valueOf(gender);
            return g;
        } catch (Exception e) {
            throw new StudentException(StudentErrors.Gender_Invalid);
        }
    }

    public static Rank validateRank(String rank) {
        try {
            Rank r = Rank.valueOf(rank);
            return r;
        } catch (Exception e) {
            throw new StudentException(StudentErrors.Rank_Invalid);
        }
    }

    public static Conduct validateConduct(String conduct) {
        try {
            Conduct c = Conduct.valueOf(conduct);
            return c;
        } catch (Exception e) {
            throw new StudentException(StudentErrors.Conduct_Invalid);
        }
    }

    public static <T> Block validateBlock(String block, Class<T> object) {
        try {
            Block b = Block.valueOf(block);
            return b;
        } catch (Exception e) {
            if (object.equals(ClassRoom.class)) {
                throw new ClassException(ClassErrors.Block_Invalid);
            } else {
                throw new SubjectException(SubjectErrors.Block_Invalid);
            }
        }
    }

    public static <T> LocalDate validateDate(String xDate, T object) {
        try {
            LocalDate date = LocalDate.parse(xDate);
            return date;
        } catch (Exception e) {
            if (object.equals(Student.class)) {
                throw new StudentException(StudentErrors.Date_Invalid);
            } else{
                throw new SubjectException(SubjectErrors.Date_Invalid);
            }
        }
    }

    public static Integer validateCredits(String credits) {
        try{
            Integer cre = Integer.parseInt(credits);
            return cre;
        }catch (Exception e){
            throw new SubjectException(SubjectErrors.Credits_Invalid);
        }
    }
}
