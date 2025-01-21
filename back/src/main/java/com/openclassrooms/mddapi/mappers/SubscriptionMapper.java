package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.SubjectDTO;
import com.openclassrooms.mddapi.dtos.SubscriptionDTO;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "subject.name", target = "subjectName") // Mappe le nom du sujet
    @Mapping(source = "user.id", target = "userId")
    SubscriptionDTO toDTO(Subscription subscription);

    @Mapping(source = "subjectId", target = "subject.id")
    @Mapping(source = "userId", target = "user.id")
    Subscription toEntity(SubscriptionDTO subscriptionDTO);
}
