package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.service.UserService;
import com.mundim.WeekMethod.view.UserView;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "jwt")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<User> (
                        userService.createUser(userDTO),
                        HttpStatus.CREATED);
    }

    @GetMapping("/find-id")
    public ResponseEntity<UserView> getUserById(@RequestParam Long userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

    @GetMapping("/find-email")
    public ResponseEntity<UserView> getUserById(@RequestParam String email) {
        User user = userService.findUserByEmail(email);
        return ResponseEntity
                .ok(new UserView(user));
    }

    @PutMapping("/update-id")
    public ResponseEntity<UserView> updatetUserById(@RequestParam Long userId, @RequestBody UserDTO userDTO) {
        User user = userService.updateUserById(userDTO, userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

    @DeleteMapping("/delete-id")
    public ResponseEntity<UserView> deleteUserById(@RequestParam Long userId) {
        User user = userService.deleteUserById(userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

}
