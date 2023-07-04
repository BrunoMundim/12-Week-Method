package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserDTO userDTO){
        return userRepository.save(new User(userDTO));
    }

    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Usuário com id: " + userId + " não existe!"));
    }

    public User updateUserById(UserDTO userDTO, Long userId){
        User user = findUserById(userId);
        if(userDTO.name() != null) user.setName(userDTO.name());
        if(userDTO.email() != null) user.setEmail(userDTO.email());
        if(userDTO.password() != null) user.setPassword(userDTO.password());
        return userRepository.save(user);
    }

    public User deleteUserById(Long userId){
        User user = findUserById(userId);
        userRepository.deleteById(userId);
        return user;
    }

}
