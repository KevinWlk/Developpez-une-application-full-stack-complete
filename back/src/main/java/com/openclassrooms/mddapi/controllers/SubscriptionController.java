package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.SubscriptionDTO;
import com.openclassrooms.mddapi.mappers.SubscriptionMapper;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
    }

    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        return ResponseEntity.ok(subscriptionMapper.toDTO(createdSubscription));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionsByUserId(@PathVariable Integer userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions.stream().map(subscriptionMapper::toDTO).collect(Collectors.toList()));
    }
}
