package com.doorstep.springproject.controller;

import com.doorstep.springproject.payloads.security.AuthenticationRequest;
import com.doorstep.springproject.payloads.security.GoogleSigninRequest;
import com.doorstep.springproject.payloads.security.SignUpRequest;
import com.doorstep.springproject.services.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author Austin Oyugi
 * @since 3/4/2021
 * @email austinoyugi@gmail.com
 */

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {

    private final AuthService authenticationService;

    @Autowired
    public AuthController( AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/getToken")
    @ApiOperation(value = "Used to generate jwt token used to enable access  to other resources ")
    public ResponseEntity<?> getAccessToken(
            @ApiParam(value = "Authentication Object") @Valid @RequestBody AuthenticationRequest authRequest){
        return authenticationService.getAccessToken(authRequest);
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register A user using Email Password")
    public ResponseEntity<?> registerUser(@ApiParam("Signup Request Object") @Valid @Validated @RequestBody SignUpRequest signUpRequest) {
        return authenticationService.registerUser(signUpRequest);
    }

    @PostMapping("/googleauthsignin")
    @ApiOperation("Authentication using Google Oauth2")
    public ResponseEntity<?> processGoogleAuthSignin(@ApiParam("GoogleSignin Request Object") GoogleSigninRequest googleSigninRequest){
        return authenticationService.processGoogleAuthSignin(googleSigninRequest);
    }

    @PostMapping("/appleauthsignin")
    @ApiOperation("Authentication using Apple Oauth2")
    public ResponseEntity<?> processAppleAuthSignin(@RequestBody Map<String, Object> appleSigninRequest){
        return authenticationService.processAppleAuthSignin(appleSigninRequest);
    }
}
