package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.SubjectDTO;
import com.openclassrooms.mddapi.mappers.SubjectMapper;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toEntity(subjectDTO);
        Subject createdSubject = subjectService.createSubject(subject);
        return ResponseEntity.ok(subjectMapper.toDTO(createdSubject));
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects.stream().map(subjectMapper::toDTO).collect(Collectors.toList()));
    }
}
