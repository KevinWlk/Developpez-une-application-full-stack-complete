package com.openclassrooms.mddapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private Integer userId;
    private String authorName;

    private Integer subjectId;
    private String subjectName;

}
