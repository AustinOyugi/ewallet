package com.doorstep.springproject.controller;

import com.doorstep.springproject.payloads.userdata.UpdatePassword;
import com.doorstep.springproject.payloads.userdata.UserSummary;
import com.doorstep.springproject.services.users.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Austin Oyugi
 * @since 3/4/2021
 * @email austinoyugi@gmail.com
 */

@RestController
@RequestMapping("/api/user/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("me/{userId}")
    @ApiOperation(value = "Get User Object by Id")
    public ResponseEntity<?> getCurrentUser(@ApiParam("userId") @PathVariable String userId) {
        return userService.getCurrentUser(userId);
    }

    @GetMapping("checkUsernameAvailability/{username}")
    @ApiOperation(value = "Check id a user withe the given username exists")
    public ResponseEntity<?> checkUsernameAvailability(@ApiParam("username") @PathVariable(value = "username") String username) {
        return userService.checkUsernameAvailability(username);
    }

    @GetMapping("checkEmailAvailability/{email}")
    @ApiOperation(value = "Check id a user withe the given email address exists")
    public ResponseEntity<?> checkEmailAvailability(@ApiParam("email") @PathVariable(value = "email") String email) {
        return userService.checkEmailAvailability(email);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get User Profile by username")
    public ResponseEntity<?> getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserProfile(username);
    }

    @PostMapping("updateprofil/{userid}")
    @ApiOperation("Update User Profile")
    public ResponseEntity<?> updateUserProfile(@ApiParam("Profile Object") UserSummary userSummary,
                                               @PathVariable("userid") String userid){
        return userService.updateUserProfile(userSummary,userid);
    }

    @PostMapping("updatepassword/{userid}")
    @ApiOperation("Update User Password")
    public ResponseEntity<?> updateUserPassword(
            @ApiParam("UpdatePassword Object") @RequestBody UpdatePassword updatePasswordObject
            , @PathVariable("userid") String userId){
        return userService.updateUserPassword(updatePasswordObject,userId);
    }
}
