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

@Service
public class AuthentificationService implements AuthentificationInterface {
    // Service qui gère l'authentification des utilisateurs
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthentificationService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse authenticate(UserRequest userRequest) throws NotFoundException {
        // Méthode pour authentifier un utilisateur et générer un jeton JWT
        Optional<User> userInDB = userRepository.findByEmail(userRequest.getEmail());
        if (userInDB.isPresent()) {
            User user = userInDB.get();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
            String jwtToken = jwtService.generateToken(user);
            return new LoginResponse(jwtToken, jwtService.getExpirationTime(), user.getId()); // Ajoutez user.getId()
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }
    }
}