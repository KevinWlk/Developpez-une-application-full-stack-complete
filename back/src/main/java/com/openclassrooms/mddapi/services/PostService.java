package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    public Post createPost(Post post) {
        User user = userRepository.findById(post.getUser().getId().intValue())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec ID : " + post.getUser().getId()));

        Subject subject = subjectRepository.findById(post.getSubject().getId().intValue())
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec ID : " + post.getSubject().getId()));

        post.setUser(user);
        post.setSubject(subject);


        return postRepository.save(post);
    }

    public Post updatePost(Integer id, Post postDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post non trouvé avec l'ID : " + id));
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        return postRepository.save(post);
    }

    public void deletePost(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post non trouvé avec l'ID : " + id));
        postRepository.delete(post);
    }

    public List<Post> getPostsBySubjectId(Integer subjectId) {
        return postRepository.findBySubjectId(subjectId);
    }

    public Post getPostById(Integer id) {
        return postRepository.findByIdWithUserAndSubject(id)
                .orElseThrow(() -> new RuntimeException("Post non trouvé avec l'ID : " + id));
    }
}
