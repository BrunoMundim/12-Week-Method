package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.service.UserService;
import com.mundim.WeekMethod.view.UserView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String validPassword = "$2a$10$ha6Kj9Q0phOCb6E7ZHWV0.6C9LMSCXMI3URivZT3B6NWPPAOPWHZe";
    private static final Long userId = 1L;
    private static final String userEmail = "email@email.com";
    private static User user;
    private static UserDTO userDTO;
    private static UserView userView;

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;


    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(userId).name("name").email(userEmail).password(validPassword)
                .registrationDate(LocalDate.now()).role("ROLE_USER").build();
        userDTO = UserDTO.builder()
                .name("name").email(userEmail).password("password")
                .build();
        userView = new UserView(user);
    }

    @Test
    public void createUser_shouldReturnCreatedUser() {
        when(userService.create(userDTO)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(userDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    public void getAllUsers_shouldReturnListOfTwoUsers() {
        when(userService.findAll()).thenReturn(Arrays.asList(user, user));

        ResponseEntity<List<UserView>> response = userController.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Arrays.asList(userView, userView));
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    public void getUserById_shouldReturnFoundUser() {
        when(userService.findById(user.getId())).thenReturn(user);

        ResponseEntity<UserView> response = userController.getUserById(user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

    @Test
    public void getUserByEmail_shouldReturnFoundUser() {
        when(userService.findByEmail(user.getEmail())).thenReturn(user);

        ResponseEntity<UserView> response = userController.getUserByEmail(user.getEmail());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

    @Test
    public void updateLoggedUser_shouldReturnUpdatedUser() {
        when(userService.updateLoggedUser(userDTO)).thenReturn(user);

        ResponseEntity<UserView> response = userController.updateLoggedUser(userDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

    @Test
    public void updateUserById_shouldReturnUpdatedUser() {
        when(userService.updateById(user.getId(), userDTO)).thenReturn(user);

        ResponseEntity<UserView> response = userController.updateUserById(userId, userDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

    @Test
    public void deleteLoggedUser_shouldReturnDeletedUser() {
        when(userService.deleteLoggedUser()).thenReturn(user);

        ResponseEntity<UserView> response = userController.deleteLoggedUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

    @Test
    public void deleteUserById_shouldReturnDeletedUser() {
        when(userService.deleteById(user.getId())).thenReturn(user);

        ResponseEntity<UserView> response = userController.deleteUserById(user.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userView);
    }

}
