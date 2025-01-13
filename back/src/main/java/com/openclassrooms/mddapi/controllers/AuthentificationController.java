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

@RestController
@RequestMapping("/api")
@Validated
public class AuthentificationController {
    private final AuthentificationService authentificationService;
    private final UserService userService;

    public AuthentificationController(AuthentificationService authService, UserService userService) {
        this.authentificationService = authService;
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public LoginResponse register(@Valid @RequestBody UserRequest userRequest) throws AlreadyExistException, NotFoundException {
        userService.createUser(userRequest);
        return authentificationService.authenticate(userRequest);
    }

    @PostMapping("/auth/login")
    public LoginResponse authenticate(@Valid @RequestBody UserRequest userRequest) throws NotFoundException {
        return authentificationService.authenticate(userRequest);
    }

    @GetMapping("/auth/me")
    public UserResponse authenticatedUser() throws NoUserInContextException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            User user = (User) authentication.getPrincipal();
            return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
        } catch (Exception e) {
            throw new NoUserInContextException("Aucun utilisateur authentifié.");
        }
    }
}
