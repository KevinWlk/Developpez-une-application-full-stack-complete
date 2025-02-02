package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.PostDTO;
import com.openclassrooms.mddapi.models.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "authorName")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "subject.name", target = "subjectName")
    PostDTO toDTO(Post post);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "subjectId", target = "subject.id")
    Post toEntity(PostDTO postDTO);
}

