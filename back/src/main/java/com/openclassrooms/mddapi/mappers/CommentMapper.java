package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.CommentDTO;
import com.openclassrooms.mddapi.dtos.PostDTO;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentDTO toDTO(Comment comment);
    Comment toEntity(CommentDTO commentDTO);
}

