package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dtos.UserRequest;
import com.openclassrooms.mddapi.dtos.UserResponse;
import com.openclassrooms.mddapi.exceptions.AlreadyExistException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.openclassrooms.mddapi.mappers.UserMapper;


@Service
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getUser(Integer id) throws NotFoundException {
        Optional<User> userInDB = userRepository.findById(id);
        if (userInDB.isPresent()) {
            User user = userInDB.get();
            return UserMapper.INSTANCE.userToUserResponse(user);
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }
    }

    @Override
    public UserResponse getUserByEmail(String email) throws NotFoundException {
        Optional<User> userInDB = userRepository.findByEmail(email);
        if (userInDB.isPresent()) {
            User user = userInDB.get();
            return UserMapper.INSTANCE.userToUserResponse(user);
        } else {
            throw new NotFoundException("Utilisateur non référencé.");
        }
    }

    // Register
    @Override
    public void createUser(UserRequest userRequest) throws AlreadyExistException {
        Optional<User> userInDB = userRepository.findByEmail(userRequest.getEmail());
        if (userInDB.isPresent()) {
            throw new AlreadyExistException("Cet email a déjà été renseigné.");
        }

        User user = UserMapper.INSTANCE.userRequestToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Encoder le mot de passe

        userRepository.save(user);

        UserMapper.INSTANCE.userToUserResponse(user); // Retourne l'utilisateur enregistré sous forme de UserResponse
    }
}
