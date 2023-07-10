package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.exception.NullFieldException;
import com.mundim.WeekMethod.repository.UserRepository;
import com.mundim.WeekMethod.security.AuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mundim.WeekMethod.exception.config.BaseErrorMessage.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";
    private static final Long userId = 1L;
    private static final String userEmail = "email@email.com";
    private static User user;
    private static UserDTO userDTO;

    @Mock
    private UserRepository userRepository;
    @Mock
    private WeekCardService weekCardService;
    @Mock
    private GoalService goalService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void setup() {
        user = User.builder()
                .id(userId).name("name").email(userEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        userDTO = UserDTO.builder()
                .name("name").email(userEmail).password("password")
                .build();
    }

    @Test
    public void create_shouldReturnCreatedUser() {
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(validPassword);

        User savedUser = userService.create(userDTO);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void createWithNullName_shouldThrowNullFieldException() {
        UserDTO nullNameUserDTO = UserDTO.builder()
                .name(null).email(userEmail).password("password")
                .build();

        Throwable thrown = catchThrowable(() -> userService.create(nullNameUserDTO));

        assertThat(thrown).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.getMessage());
    }

    @Test
    public void createWithNullEmail_shouldThrowNullFieldException() {
        UserDTO nullEmailUserDTO = UserDTO.builder()
                .name("name").email(null).password("password")
                .build();

        Throwable thrown = catchThrowable(() -> userService.create(nullEmailUserDTO));

        assertThat(thrown).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.getMessage());
    }

    @Test
    public void createWithNullPassword_shouldThrowNullFieldException() {
        UserDTO nullPasswordUserDTO = UserDTO.builder()
                .name("name").email(userEmail).password(null)
                .build();

        Throwable thrown = catchThrowable(() -> userService.create(nullPasswordUserDTO));

        assertThat(thrown).isInstanceOf(NullFieldException.class)
                .hasMessage(NULL_FIELD.getMessage());
    }

    @Test
    public void createWithInvalidEmail_shouldThrowBadRequestException() {
        UserDTO invalidEmailUserDTO = UserDTO.builder()
                .name("name").email("invalidFormatEmail").password("password")
                .build();

        Throwable thrown = catchThrowable(() -> userService.create(invalidEmailUserDTO));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage(EMAIL_INVALID_FORMAT.getMessage());
    }

    @Test
    public void findAll_shouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user, user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertThat(foundUsers).isNotNull();
        assertThat(foundUsers.size()).isEqualTo(2);
        assertThat(foundUsers.contains(user)).isTrue();
    }

    @Test
    public void findById_shouldReturnNotNullUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        User foundUser = userService.findById(userId);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void findById_shouldThrowBadRequestException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> userService.findById(userId));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage(USER_NOT_FOUND_BY_ID.params(userId.toString()).getMessage());
    }

    @Test
    public void findByEmail_shouldReturnFoundUser() {
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        User foundUser = userService.findByEmail(userEmail);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void findByEmail_shouldThrowBadRequestException() {
        when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(null);

        Throwable thrown = catchThrowable(() -> userService.findByEmail(userEmail));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage(USER_NOT_FOUND_BY_EMAIL.params(userEmail).getMessage());
    }

    @Test
    public void updateById_shouldReturnUpdatedUser() {
        UserDTO updatedUserDTO = UserDTO.builder()
                .name("new name").email(userEmail.concat(".br")).password("new password")
                .build();

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(validPassword);
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);

        User updatedUser = userService.updateById(userId, updatedUserDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("new name");
        assertThat(updatedUser.getEmail()).isEqualTo(userEmail.concat(".br"));
        assertThat(updatedUser.getPassword()).isNotNull();
    }

    @Test
    public void updateById_shouldNotUpdateNullField() {
        UserDTO updatedUserDTO = UserDTO.builder()
                .name(null).email(null).password(null)
                .build();

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);

        User updatedUser = userService.updateById(userId, updatedUserDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isNotNull();
        assertThat(updatedUser.getEmail()).isNotNull();
        assertThat(updatedUser.getPassword()).isNotNull();
    }

    @Test
    public void updateLoggedUser_shouldReturnUpdatedUser() {
        UserDTO updatedUserDTO = UserDTO.builder()
                .name("new name").email(userEmail.concat(".br")).password("new password")
                .build();

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(Mockito.any(String.class))).thenReturn(validPassword);
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        User updatedUser = userService.updateLoggedUser(updatedUserDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("new name");
        assertThat(updatedUser.getEmail()).isEqualTo(userEmail.concat(".br"));
        assertThat(updatedUser.getPassword()).isNotNull();
    }

    @Test
    public void updateLoggedUser_shouldNotUpdateNullField() {
        UserDTO updatedUserDTO = UserDTO.builder()
                .name(null).email(null).password(null)
                .build();

        when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);
        when(authenticationService.findUserByBearer()).thenReturn(user);

        User updatedUser = userService.updateLoggedUser(updatedUserDTO);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isNotNull();
        assertThat(updatedUser.getEmail()).isNotNull();
        assertThat(updatedUser.getPassword()).isNotNull();
    }

    @Test
    public void deleteById_shouldReturnDeletedUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);

        User deletedUser = userService.deleteById(user.getId());

        assertThat(deletedUser).isNotNull();
        assertThat(deletedUser).isEqualTo(user);
    }

    @Test
    public void deleteLoggedUser_shouldReturnDeletedUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(authenticationService.findUserByBearer()).thenReturn(user);
        when(authenticationService.verifyUserAuthentication(Mockito.any(User.class))).thenReturn(true);

        User deletedUser = userService.deleteLoggedUser();

        assertThat(deletedUser).isNotNull();
        assertThat(deletedUser).isEqualTo(user);
    }

}
