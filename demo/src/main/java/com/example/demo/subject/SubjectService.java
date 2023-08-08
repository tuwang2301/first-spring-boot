package com.example.demo.subject;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    public List<Subject> getSubjects() {
        return  subjectRepository.findAll();
    }

    public void addSubject(Subject subject) {
        Optional<Subject> found = subjectRepository.findByName(subject.getName());
        if(found.isPresent()){
            throw new IllegalStateException();
        }
        subjectRepository.save(subject);
    }
    @Transactional
    public int updateSubject(Long subjectId, String name, Integer credits) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if(!found.isPresent()){
            return 1;
        }
        for (Subject s: subjectRepository.findAll()) {
            if(s.getId() != subjectId && s.getName().equalsIgnoreCase(name)){
                return 2;
            }
        }
        if(name != null && name.length() > 0 && !Objects.equals(found.get().getName(),name)){
            found.get().setName(name);
        }
        return 0;
    }


    public void deleteSubject(Long subjectId) {
        Optional<Subject> found = subjectRepository.findById(subjectId);
        if(!found.isPresent()){
            throw new IllegalStateException();
        }
        subjectRepository.deleteById(subjectId);
    }
}
