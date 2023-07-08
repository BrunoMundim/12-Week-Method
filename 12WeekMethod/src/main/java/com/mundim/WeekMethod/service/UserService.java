package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.UserRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.password());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Usuário com id: " + userId + " não existe!"));
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) throw new BadRequestException("User with email not found");
        return user;
    }

    public User updateUserById(UserDTO userDTO, Long userId) {
        User user = findUserById(userId);
        authenticationService.verifyUserAuthentication(user);
        if (userDTO.name() != null) user.setName(userDTO.name());
        if (userDTO.email() != null) user.setEmail(userDTO.email());
        if (userDTO.password() != null) {
            String encodedPassword = passwordEncoder.encode(userDTO.password());
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    public User updateLoggedUser(UserDTO userDTO){
        User user = authenticationService.findUserByBearer();
        return updateUserById(userDTO, user.getId());
    }

    public User deleteUserById(Long userId) {
        User user = findUserById(userId);
        authenticationService.verifyUserAuthentication(user);
        userRepository.deleteById(userId);
        return user;
    }

    public User deleteLoggedUser() {
        User user = authenticationService.findUserByBearer();
        return deleteUserById(user.getId());
    }

}
