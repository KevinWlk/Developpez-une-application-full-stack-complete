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

/**
 * Contrôleur REST gérant les opérations relatives à l'entité {@link Subject}.
 * Permet notamment de créer de nouveaux sujets et de récupérer la liste
 * de tous les sujets existants.
 */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    /**
     * Initialise le contrôleur avec le service et le mapper nécessaires
     * pour gérer les entités {@link Subject} et effectuer la conversion
     * entre {@link Subject} et {@link SubjectDTO}.
     *
     * @param subjectService service de gestion de la logique métier
     *                       pour l'entité Subject
     * @param subjectMapper  composant permettant de mapper entre
     *                       {@link Subject} et {@link SubjectDTO}
     */
    @Autowired
    public SubjectController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    /**
     * Crée un nouveau sujet à partir des données reçues dans le corps
     * de la requête (format JSON), puis renvoie le sujet créé sous forme
     * de {@link SubjectDTO}.
     *
     * @param subjectDTO le DTO contenant les informations du sujet à créer
     * @return un {@link ResponseEntity} contenant le {@link SubjectDTO} du sujet créé
     */
    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toEntity(subjectDTO);
        Subject createdSubject = subjectService.createSubject(subject);
        return ResponseEntity.ok(subjectMapper.toDTO(createdSubject));
    }

    /**
     * Récupère la liste de tous les sujets existants, puis renvoie ces
     * informations sous forme de liste de {@link SubjectDTO}.
     *
     * @return un {@link ResponseEntity} contenant la liste des {@link SubjectDTO}
     */
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(
                subjects.stream()
                        .map(subjectMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
