package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.CommentDTO;
import com.openclassrooms.mddapi.models.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Interface MapStruct utilisée pour convertir un objet {@link Comment}
 * en un objet {@link CommentDTO} et inversement.
 * <p>
 * L'annotation {@code @Mapper} génère automatiquement une implémentation de cet
 * interface lors de la compilation, permettant de réduire le code boilerplate.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Convertit un objet {@link Comment} en un objet {@link CommentDTO}.
     * <p>
     * Les champs {@code post.id} et {@code user.id} sont mappés respectivement
     * vers {@code postId} et {@code userId} dans le DTO.
     *
     * @param comment l'objet modèle source à convertir
     * @return un objet {@link CommentDTO} contenant les données converties
     */
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt")
    CommentDTO toDTO(Comment comment);

    /**
     * Convertit un objet {@link CommentDTO} en un objet {@link Comment}.
     * <p>
     * Les champs {@code postId} et {@code userId} sont mappés
     * respectivement vers les champs {@code post.id} et {@code user.id}.
     * De plus, le champ {@code createdAt} est initialisé à la date
     * et l'heure actuelles via {@code LocalDateTime.now()}.
     *
     * @param commentDTO l'objet DTO source à convertir
     * @return un objet {@link Comment} contenant les données converties
     */
    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Comment toEntity(CommentDTO commentDTO);
}
