package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.SubjectDTO;
import com.openclassrooms.mddapi.models.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDTO toDTO(Subject subject);
    Subject toEntity(SubjectDTO subjectDTO);
}
