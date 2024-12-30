package com.openclassrooms.mddapi.mappers;

import com.openclassrooms.mddapi.dtos.SubjectDTO;
import com.openclassrooms.mddapi.dtos.SubscriptionDTO;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionDTO toDTO(Subscription subscription);
    Subscription toEntity(SubscriptionDTO subscriptionDTO);
}
