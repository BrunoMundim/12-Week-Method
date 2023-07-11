package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.UserRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.*;

@Service
public class UserService {

    private static final String emailExpectedFormat = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final WeekCardService weekCardService;
    private final GoalService goalService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationService authenticationService,
            @Lazy WeekCardService weekCardService,
            @Lazy GoalService goalService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.weekCardService = weekCardService;
        this.goalService = goalService;
    }

    public User create(UserDTO userDTO) {
        verifyDto(userDTO);
        User user = new User(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user = userRepository.save(user);
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(
                        USER_NOT_FOUND_BY_ID
                                .params(userId.toString())
                                .getMessage()));
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new BadRequestException(USER_NOT_FOUND_BY_EMAIL.params(email).getMessage());
        return user;
    }

    public User updateById(Long userId, UserDTO userDTO) {
        User user = findById(userId);
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
        findById(user.getId()); // Verify if user is valid
        return updateById(user.getId(), userDTO);
    }

    public User deleteById(Long userId) {
        User user = findById(userId);
        authenticationService.verifyUserAuthentication(user);
        weekCardService.deleteAllWeekCardByUserId(userId);
        goalService.deleteByUserId(userId);
        userRepository.deleteById(userId);
        return user;
    }

    public User deleteLoggedUser() {
        User user = authenticationService.findUserByBearer();
        return deleteById(user.getId());
    }

    private void verifyDto(UserDTO dto) {
        if(dto.name() == null) throw new NullFieldException(NULL_FIELD.params("'name'").getMessage());
        if(dto.email() == null) throw new NullFieldException(NULL_FIELD.params("'email'").getMessage());
        if(dto.password() == null) throw new NullFieldException(NULL_FIELD.params("'password'").getMessage());
        if(!Pattern.compile(emailExpectedFormat).matcher(dto.email()).matches())
            throw new BadRequestException(EMAIL_INVALID_FORMAT.getMessage());
    }

}
