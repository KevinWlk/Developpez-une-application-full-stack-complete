package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.LoginResponse;
import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.exceptions.NotFoundException;

/**
 * Interface définissant la méthode d'authentification d'un utilisateur
 * en fonction de ses identifiants.
 */
public interface AuthentificationInterface {

    /**
     * Authentifie un utilisateur à partir de ses identifiants (par exemple, email
     * et mot de passe), et renvoie un {@link LoginResponse} contenant le jeton JWT
     * ainsi que d'éventuelles informations associées.
     *
     * @param userRequest un objet contenant les identifiants de l'utilisateur
     * @return un objet {@link LoginResponse} si l'authentification réussit
     * @throws NotFoundException si l'utilisateur correspondant aux identifiants
     *                           fournis n'est pas trouvé
     */
    LoginResponse authenticate(UserRequest userRequest) throws NotFoundException;
}
