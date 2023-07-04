package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.UserDTO;
import com.mundim.WeekMethod.entity.User;
import com.mundim.WeekMethod.service.UserService;
import com.mundim.WeekMethod.view.UserView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Api(tags = "users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation(value = "Create user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<User> (
                        userService.createUser(userDTO),
                        HttpStatus.CREATED);
    }

    @GetMapping("/find-id")
    @ApiOperation(value = "Find user by ID")
    public ResponseEntity<UserView> getUserById(@ApiParam(value = "User ID") @RequestParam Long userId) {
        User user = userService.findUserById(userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

    @PutMapping("/update-id")
    @ApiOperation(value = "Update user by ID")
    public ResponseEntity<UserView> updatetUserById(@RequestParam Long userId, @RequestBody UserDTO userDTO) {
        User user = userService.updateUserById(userDTO, userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

    @DeleteMapping("/delete-id")
    @ApiOperation(value = "Delete user by ID")
    public ResponseEntity<UserView> deleteUserById(@RequestParam Long userId) {
        User user = userService.deleteUserById(userId);
        return ResponseEntity
                .ok(new UserView(user));
    }

}
