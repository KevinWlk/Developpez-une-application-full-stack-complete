package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.LoginResponse;
import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.exceptions.NotFoundException;

public interface AuthentificationInterface {
    LoginResponse authenticate(UserRequest userRequest) throws NotFoundException;
}
