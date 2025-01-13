package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.CommentDTO;
import com.openclassrooms.mddapi.dtos.SubjectDTO;
import com.openclassrooms.mddapi.mappers.CommentMapper;
import com.openclassrooms.mddapi.mappers.SubjectMapper;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.services.CommentService;
import com.openclassrooms.mddapi.services.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        logger.info("Received request to create a comment: {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        logger.info("Mapped DTO to entity: {}", comment);
        Comment createdComment = commentService.createComment(comment);
        logger.info("Created comment: {}", createdComment);
        return ResponseEntity.ok(commentMapper.toDTO(createdComment));
    }

}

//@RestController
//@RequestMapping("/api/comments")

//
//    @PostMapping
//    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
//        Comment comment = commentMapper.toEntity(commentDTO);
//        Comment createdComment = commentService.createComment(comment);
//        return ResponseEntity.ok(commentMapper.toDTO(createdComment));
//    }
//
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Integer postId) {
//        List<Comment> comments = commentService.getCommentsByPostId(postId);
//        return ResponseEntity.ok(comments.stream().map(commentMapper::toDTO).collect(Collectors.toList()));
//    }
//}