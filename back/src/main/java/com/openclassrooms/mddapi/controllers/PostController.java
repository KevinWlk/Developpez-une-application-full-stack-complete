package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.PostDTO;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST gérant les opérations CRUD (création, lecture, mise à jour,
 * suppression) sur les entités {@link Post}. Permet également de filtrer les
 * posts par sujet (subjectId).
 */
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    /**
     * Initialise le contrôleur avec le service et le mapper nécessaires à la gestion
     * des entités {@link Post} et de leur conversion en {@link PostDTO}.
     *
     * @param postService service de gestion des posts
     * @param postMapper  composant pour mapper {@link Post} et {@link PostDTO}
     */
    @Autowired
    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    /**
     * Crée un nouveau post à partir des données fournies dans le corps de la requête,
     * puis renvoie le post créé sous forme de {@link PostDTO}.
     *
     * @param postDTO le DTO contenant les informations du post à créer
     * @return un {@link ResponseEntity} contenant le {@link PostDTO} du post créé
     */
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(postMapper.toDTO(createdPost));
    }

    /**
     * Met à jour un post existant identifié par son ID avec les nouvelles données
     * contenues dans {@code postDTO}.
     *
     * @param id      l'ID du post à mettre à jour
     * @param postDTO le DTO contenant les nouvelles informations du post
     * @return un {@link ResponseEntity} contenant le {@link PostDTO} du post mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Integer id,
                                              @RequestBody PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        Post updatedPost = postService.updatePost(id, post);
        return ResponseEntity.ok(postMapper.toDTO(updatedPost));
    }

    /**
     * Récupère un post à partir de son ID, puis renvoie les données associées
     * sous forme de {@link PostDTO}.
     *
     * @param id l'ID du post à récupérer
     * @return un {@link ResponseEntity} contenant le {@link PostDTO} du post récupéré
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(postMapper.toDTO(post));
    }

    /**
     * Supprime un post identifié par son ID.
     *
     * @param id l'ID du post à supprimer
     * @return un {@link ResponseEntity} sans contenu (204 No Content) après suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère tous les posts associés à un sujet spécifique (subjectId), puis renvoie
     * la liste sous forme de {@link PostDTO}.
     *
     * @param subjectId l'ID du sujet pour lequel on souhaite récupérer les posts
     * @return un {@link ResponseEntity} contenant la liste des {@link PostDTO}
     */
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PostDTO>> getPostsBySubjectId(@PathVariable Integer subjectId) {
        List<Post> posts = postService.getPostsBySubjectId(subjectId);
        return ResponseEntity.ok(
                posts.stream()
                        .map(postMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
