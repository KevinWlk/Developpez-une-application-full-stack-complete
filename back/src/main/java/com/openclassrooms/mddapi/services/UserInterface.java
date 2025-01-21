package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.dtos.UserResponse;
import com.openclassrooms.mddapi.exceptions.AlreadyExistException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;

public interface UserInterface {

    UserResponse getUser(Integer id) throws NotFoundException;

    UserResponse getUserByEmail(String email) throws NotFoundException;

    // Register
    void createUser(UserRequest userRequest) throws AlreadyExistException;

    //update
    UserResponse updateUser(Integer id, UserRequest userRequest) throws NotFoundException;



}