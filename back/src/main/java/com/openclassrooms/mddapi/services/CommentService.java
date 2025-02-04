package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant la logique métier liée aux {@link Comment}.
 * Il fait appel au {@link CommentRepository} pour interagir
 * avec la base de données.
 */
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * Initialise le service avec le {@link CommentRepository} nécessaire
     * pour effectuer les opérations CRUD sur les commentaires.
     *
     * @param commentRepository référentiel Spring Data JPA pour {@link Comment}
     */
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Crée un nouveau commentaire et l'enregistre en base de données.
     *
     * @param comment l'objet commentaire à créer
     * @return le commentaire créé (avec un ID généré)
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * Récupère la liste de tous les commentaires associés à un post spécifique,
     * identifié par son ID.
     *
     * @param postId l'identifiant du post
     * @return une liste de {@link Comment} correspondant à ce post
     */
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Met à jour le contenu d'un commentaire existant, identifié par son ID.
     *
     * @param id         l'identifiant du commentaire à mettre à jour
     * @param newContent le nouveau contenu textuel du commentaire
     * @return le commentaire mis à jour
     * @throws IllegalArgumentException si le commentaire n'est pas trouvé
     */
    public Comment updateComment(Long id, String newContent) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + id));
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    /**
     * Supprime un commentaire de la base de données, en fonction de son ID.
     *
     * @param id l'identifiant du commentaire à supprimer
     * @throws IllegalArgumentException si aucun commentaire n'est trouvé avec cet ID
     */
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }
}
