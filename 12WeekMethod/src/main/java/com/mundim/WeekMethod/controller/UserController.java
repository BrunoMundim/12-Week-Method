package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.service.UserService;
import com.mundim.WeekMethod.view.UserView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

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
    @Operation(tags = "User", summary = "Create a user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<User> (
                        userService.create(userDTO),
                        HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Find all users (ADMIN ONLY)")
    public ResponseEntity<List<UserView>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserView> usersViews = new ArrayList<>();
        for (User user : users) {
            usersViews.add(new UserView(user));
        }
        return ResponseEntity.ok(usersViews);
    }

    @GetMapping("/find-id")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Find user by id (ADMIN ONLY)")
    public ResponseEntity<UserView> getUserById(@RequestParam Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserView(user));
    }

    @GetMapping("/find-email")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Find user by email (ADMIN ONLY)")
    public ResponseEntity<UserView> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(new UserView(user));
    }

    @PutMapping("/update")
    @Operation(tags = "User", summary = "Update logged user")
    public ResponseEntity<UserView> updateLoggedUser(@RequestBody UserDTO userDTO) {
        User user = userService.updateLoggedUser(userDTO);
        return ResponseEntity.ok(new UserView(user));
    }

    @PutMapping("/update-id")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Update user by id (ADMIN ONLY)")
    public ResponseEntity<UserView> updateUserById(@RequestParam Long userId, @RequestBody UserDTO userDTO) {
        User user = userService.updateById(userId, userDTO);
        return ResponseEntity.ok(new UserView(user));
    }

    @DeleteMapping
    @Operation(tags = "User", summary = "Delete logged user")
    public ResponseEntity<UserView> deleteLoggedUser() {
        User user = userService.deleteLoggedUser();
        return ResponseEntity.ok(new UserView(user));
    }

    @DeleteMapping("/delete-id")
    @RolesAllowed("ADMIN")
    @Operation(tags = "User", summary = "Delete user by id (ADMIN ONLY)")
    public ResponseEntity<UserView> deleteUserById(@RequestParam Long userId) {
        User user = userService.deleteById(userId);
        return ResponseEntity.ok(new UserView(user));
    }

}
