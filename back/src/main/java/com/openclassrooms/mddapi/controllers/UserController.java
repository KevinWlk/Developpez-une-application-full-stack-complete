package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.dtos.UserResponse;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public UserResponse getUser(@PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id) throws NotFoundException {
        return userService.getUser(id);
    }
    @PutMapping("/user/{id}")
    public UserResponse updateUser(
            @PathVariable @Min(value = 1, message = "L'identifiant doit être égal ou supérieur à un (1).") int id,
            @RequestBody UserRequest userRequest) throws NotFoundException {
        return userService.updateUser(id, userRequest);
    }

}
