package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface Spring Data JPA pour la gestion des entités {@link Comment}.
 * <p>
 * Fournit des méthodes de base (CRUD) héritées de {@link JpaRepository}
 * et définit une méthode de requête personnalisée pour récupérer des
 * commentaires par l'identifiant du post.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Récupère la liste de tous les commentaires liés à un post identifié
     * par {@code postId}.
     *
     * @param postId l'identifiant du post
     * @return une liste de {@link Comment} associés au post correspondant
     */
    List<Comment> findByPostId(Integer postId);
}
