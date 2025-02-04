package com.openclassrooms.mddapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) représentant un commentaire.
 * Il est utilisé pour transférer les données relatives à un
 * commentaire entre les couches de l'application (contrôleur, service, etc.).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    /**
     * Identifiant unique du commentaire.
     */
    private Long id;

    /**
     * Identifiant du post auquel ce commentaire est associé.
     */
    private Integer postId;

    /**
     * Identifiant de l'utilisateur ayant créé le commentaire.
     */
    private Integer userId;

    /**
     * Nom de l'utilisateur ayant créé le commentaire.
     */
    private String userName;

    /**
     * Contenu textuel du commentaire.
     */
    private String content;

    /**
     * Date et heure de création du commentaire.
     */
    private LocalDateTime createdAt;
}
