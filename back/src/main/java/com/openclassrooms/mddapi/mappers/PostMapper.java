package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.PostDTO;
import com.openclassrooms.mddapi.models.Post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "subject.name", target = "subjectName") // Mappe le nom du sujet
    @Mapping(source = "user.id", target = "userId")
    PostDTO toDTO(Post post);

    @Mapping(source = "subjectId", target = "subject.id")
    @Mapping(source = "userId", target = "user.id")
    Post toEntity(PostDTO postDTO);
}
