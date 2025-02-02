package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.LoginResponse;
import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.dtos.UserResponse;
import com.openclassrooms.mddapi.exceptions.AlreadyExistException;
import com.openclassrooms.mddapi.exceptions.NoUserInContextException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.services.AuthentificationService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les endpoints d'authentification (enregistrement, connexion et récupération
 * d'informations sur l'utilisateur authentifié).
 */
@RestController
@RequestMapping("/api")
@Validated
public class AuthentificationController {

    private final AuthentificationService authentificationService;
    private final UserService userService;

    /**
     * Crée une nouvelle instance du contrôleur avec les services
     * d'authentification et de gestion des utilisateurs.
     *
     * @param authService service gérant la logique d'authentification
     * @param userService service gérant la création et la récupération des utilisateurs
     */
    public AuthentificationController(AuthentificationService authService, UserService userService) {
        this.authentificationService = authService;
        this.userService = userService;
    }

    /**
     * Enregistre un nouvel utilisateur à partir d'un {@link UserRequest} et renvoie
     * un jeton d'authentification via un {@link LoginResponse}.
     *
     * @param userRequest les informations nécessaires pour créer un nouvel utilisateur
     * @return l'objet contenant le token d'authentification
     * @throws AlreadyExistException si l'utilisateur existe déjà
     * @throws NotFoundException     si l'utilisateur n'a pas pu être créé
     */
    @PostMapping("/auth/register")
    public LoginResponse register(@Valid @RequestBody UserRequest userRequest)
            throws AlreadyExistException, NotFoundException {
        userService.createUser(userRequest);
        return authentificationService.authenticate(userRequest);
    }

    /**
     * Authentifie un utilisateur existant à partir d'un {@link UserRequest} et
     * renvoie un {@link LoginResponse} contenant le jeton d'authentification.
     *
     * @param userRequest les identifiants (email, mot de passe) de l'utilisateur
     * @return l'objet contenant le token d'authentification
     * @throws NotFoundException si l'utilisateur n'est pas trouvé
     */
    @PostMapping("/auth/login")
    public LoginResponse authenticate(@Valid @RequestBody UserRequest userRequest)
            throws NotFoundException {
        return authentificationService.authenticate(userRequest);
    }

    /**
     * Récupère les informations de l'utilisateur actuellement authentifié dans
     * le contexte de sécurité (SecurityContext).
     *
     * @return l'objet {@link UserResponse} représentant l'utilisateur authentifié
     * @throws NoUserInContextException si aucun utilisateur n'est trouvé dans le contexte
     */
    @GetMapping("/auth/me")
    public UserResponse authenticatedUser() throws NoUserInContextException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) authentication.getPrincipal();
            return new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        } catch (Exception e) {
            throw new NoUserInContextException("Aucun utilisateur authentifié.");
        }
    }
}
