package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findBySubjectId(Integer subjectId);

    @Query("SELECT p FROM Post p JOIN FETCH p.user JOIN FETCH p.subject WHERE p.id = :id")
    Optional<Post> findByIdWithUserAndSubject(@Param("id") Integer id);
}