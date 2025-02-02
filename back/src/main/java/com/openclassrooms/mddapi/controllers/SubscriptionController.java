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

/**
 * Contrôleur REST gérant les opérations liées aux abonnements
 * ({@link Subscription}). Permet de créer, récupérer (par ID d'utilisateur)
 * et supprimer des abonnements.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    /**
     * Initialise le contrôleur en injectant le service de gestion des abonnements
     * et le mapper pour la conversion entité/DTO.
     *
     * @param subscriptionService service de logique métier pour les abonnements
     * @param subscriptionMapper  composant permettant de mapper entre
     *                            {@link Subscription} et {@link SubscriptionDTO}
     */
    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService,
                                  SubscriptionMapper subscriptionMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionMapper = subscriptionMapper;
    }

    /**
     * Crée un nouvel abonnement à partir des données fournies dans le corps
     * de la requête (format JSON).
     *
     * @param subscriptionDTO les informations du nouvel abonnement
     * @return un {@link ResponseEntity} contenant le {@link SubscriptionDTO}
     *         de l'abonnement créé
     */
    @PostMapping
    public ResponseEntity<SubscriptionDTO> createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO);
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        return ResponseEntity.ok(subscriptionMapper.toDTO(createdSubscription));
    }

    /**
     * Récupère la liste des abonnements pour un utilisateur donné
     * (identifié par {@code userId}).
     *
     * @param userId l'identifiant de l'utilisateur dont on veut récupérer
     *               les abonnements
     * @return un {@link ResponseEntity} contenant la liste de {@link SubscriptionDTO}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionsByUserId(@PathVariable Integer userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(
                subscriptions.stream()
                        .map(subscriptionMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Supprime un abonnement identifié par son ID.
     *
     * @param id l'identifiant de l'abonnement à supprimer
     * @return un {@link ResponseEntity} sans contenu (No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
