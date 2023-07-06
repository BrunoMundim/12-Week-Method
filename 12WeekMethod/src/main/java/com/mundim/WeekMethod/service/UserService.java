package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        String encodedPassword = passwordEncoder.encode(userDTO.password());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
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
        verifyAuthentication(user);
        if (userDTO.name() != null) user.setName(userDTO.name());
        if (userDTO.email() != null) user.setEmail(userDTO.email());
        if (userDTO.password() != null) {
            String encodedPassword = passwordEncoder.encode(userDTO.password());
            user.setPassword(encodedPassword);
        }
        return userRepository.save(user);
    }

    public User deleteUserById(Long userId) {
        try {
            User user = findUserById(userId);
            verifyAuthentication(user);
            userRepository.deleteById(userId);
            return user;
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }

    }

    public void verifyAuthentication(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
        if(!currentPrincipalName.equals(user.getEmail())){
            throw new BadRequestException("Não autorizado!");
        }
    }

}
