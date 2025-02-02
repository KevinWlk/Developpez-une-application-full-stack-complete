package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.dtos.UserResponse;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les opérations liées aux utilisateurs
 * (consultation et mise à jour d'un utilisateur).
 */
@RestController
@Validated
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * Initialise le contrôleur avec le service de gestion des utilisateurs.
     *
     * @param userService service gérant la logique métier pour les utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère les informations d'un utilisateur spécifique,
     * identifié par son ID.
     *
     * @param id l'identifiant de l'utilisateur (doit être >= 1)
     * @return un objet {@link UserResponse} contenant les informations de l'utilisateur
     * @throws NotFoundException si l'utilisateur n'a pas été trouvé
     */
    @GetMapping("/user/{id}")
    public UserResponse getUser(
            @PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).")
            int id
    ) throws NotFoundException {
        return userService.getUser(id);
    }

    /**
     * Met à jour les informations d'un utilisateur (nom, email, etc.)
     * identifié par son ID.
     *
     * @param id          l'identifiant de l'utilisateur (doit être >= 1)
     * @param userRequest le corps de la requête contenant les nouvelles données de l'utilisateur
     * @return un objet {@link UserResponse} mis à jour
     * @throws NotFoundException si l'utilisateur à mettre à jour n'a pas été trouvé
     */
    @PutMapping("/user/{id}")
    public UserResponse updateUser(
            @PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).")
            int id,
            @RequestBody UserRequest userRequest
    ) throws NotFoundException {
        return userService.updateUser(id, userRequest);
    }
}
