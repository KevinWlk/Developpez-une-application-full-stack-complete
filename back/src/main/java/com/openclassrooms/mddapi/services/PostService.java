package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getPostsBySubjectId(Integer subjectId) {
        return postRepository.findBySubjectId(subjectId);
    }
}
