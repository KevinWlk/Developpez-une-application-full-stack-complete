package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.PostDTO;
import com.openclassrooms.mddapi.dtos.SubscriptionDTO;
import com.openclassrooms.mddapi.mappers.PostMapper;
import com.openclassrooms.mddapi.mappers.SubscriptionMapper;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.services.PostService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(postMapper.toDTO(createdPost));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PostDTO>> getPostsBySubjectId(@PathVariable Integer subjectId) {
        List<Post> posts = postService.getPostsBySubjectId(subjectId);
        return ResponseEntity.ok(posts.stream().map(postMapper::toDTO).collect(Collectors.toList()));
    }
}
