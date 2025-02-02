package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.LoginResponse;
import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsable de l'authentification des utilisateurs.
 * Il valide les identifiants fournis et génère un jeton JWT
 * pour les utilisateurs correctement authentifiés.
 */
@Service
public class AuthentificationService implements AuthentificationInterface {

    /**
     * Référentiel pour accéder aux données des utilisateurs.
     */
    private final UserRepository userRepository;

    /**
     * Composant Spring Security gérant l'authentification (vérification
     * du couple email/mot de passe).
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Service dédié à la génération et la validation de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Constructeur injectant les dépendances nécessaires pour l'authentification.
     *
     * @param userRepository        référentiel pour la gestion des {@link User}
     * @param authenticationManager composant pour l'authentification via Spring Security
     * @param jwtService            service pour la génération/validation de tokens JWT
     */
    public AuthentificationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Authentifie un utilisateur à partir de ses identifiants (email, mot de passe),
     * puis génère un jeton JWT en cas de succès.
     *
     * @param userRequest objet contenant l'email et le mot de passe de l'utilisateur
     * @return un {@link LoginResponse} contenant le token JWT,
     *         le temps d'expiration associé et l'ID de l'utilisateur
     * @throws NotFoundException si l'utilisateur n'est pas trouvé en base de données
     */
    @Override
    public LoginResponse authenticate(UserRequest userRequest) throws NotFoundException {
        // Vérifie si l'utilisateur existe via son email.
        Optional<User> userInDB = userRepository.findByEmail(userRequest.getEmail());
        if (userInDB.isPresent()) {
            User user = userInDB.get();

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getEmail(),
                            userRequest.getPassword()
                    )
            );

            // Génération d'un token JWT pour l'utilisateur authentifié.
            String jwtToken = jwtService.generateToken(user);
            return new LoginResponse(jwtToken, jwtService.getExpirationTime(), user.getId());
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }
    }
}
