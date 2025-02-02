package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.CommentDTO;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur gérant les opérations CRUD (création, lecture, mise à jour et suppression)
 * sur les commentaires. Les commentaires sont liés à un post identifié par son ID.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    /**
     * Initialise le contrôleur avec les services nécessaires (service de gestion
     * des commentaires et mapper pour la conversion entité/DTO).
     *
     * @param commentService service gérant la logique métier des commentaires
     * @param commentMapper  composant permettant de mapper un Comment en CommentDTO et inversement
     */
    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    /**
     * Crée un nouveau commentaire à partir des données reçues dans le corps de la requête.
     * Le nouveau commentaire est ensuite renvoyé sous forme de {@link CommentDTO}.
     *
     * @param commentDTO le DTO contenant les informations du commentaire à créer
     * @return une réponse HTTP contenant le commentaire créé
     */
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        logger.info("Received request to create a comment: {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        Comment createdComment = commentService.createComment(comment);
        return ResponseEntity.ok(commentMapper.toDTO(createdComment));
    }

    /**
     * Récupère la liste de tous les commentaires associés à un post spécifique,
     * identifié par son ID.
     *
     * @param postId l'identifiant du post pour lequel on souhaite récupérer les commentaires
     * @return une réponse HTTP contenant la liste des commentaires sous forme de {@link CommentDTO}
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(
                comments.stream().map(commentMapper::toDTO).collect(Collectors.toList())
        );
    }

    /**
     * Met à jour le contenu d'un commentaire existant identifié par son ID.
     * Renvoie ensuite le commentaire mis à jour sous forme de {@link CommentDTO}.
     *
     * @param id         l'identifiant du commentaire à mettre à jour
     * @param commentDTO un DTO contenant le nouveau contenu du commentaire
     * @return une réponse HTTP contenant le commentaire mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDTO commentDTO
    ) {
        Comment updatedComment = commentService.updateComment(id, commentDTO.getContent());
        return ResponseEntity.ok(commentMapper.toDTO(updatedComment));
    }

    /**
     * Supprime un commentaire existant identifié par son ID.
     *
     * @param id l'identifiant du commentaire à supprimer
     * @return une réponse HTTP sans contenu (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
