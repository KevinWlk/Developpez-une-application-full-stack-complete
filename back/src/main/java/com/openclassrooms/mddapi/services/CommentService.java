package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment createComment(Comment comment) {
        logger.info("Attempting to save comment: {}", comment);
        // Vérifier que le post et l'utilisateur existent
        comment.setPost(postRepository.findById(Math.toIntExact(comment.getPost().getId()))
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + comment.getPost().getId())));
        comment.setUser(userRepository.findById(Math.toIntExact(comment.getUser().getId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + comment.getUser().getId())));

        // Enregistrer le commentaire
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment saved successfully: {}", savedComment);
        return savedComment;
    }
}

