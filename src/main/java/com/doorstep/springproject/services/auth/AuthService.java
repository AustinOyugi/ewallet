package com.doorstep.springproject.services.auth;


import com.doorstep.springproject.enums.RoleName;
import com.doorstep.springproject.models.userdata.Role;
import com.doorstep.springproject.models.userdata.User;
import com.doorstep.springproject.payloads.security.AuthenticationRequest;
import com.doorstep.springproject.payloads.security.GoogleSigninRequest;
import com.doorstep.springproject.payloads.security.JwtAuthenticationResponse;
import com.doorstep.springproject.payloads.security.SignUpRequest;
import com.doorstep.springproject.payloads.system.ApiResponse;
import com.doorstep.springproject.repositories.RoleRepository;
import com.doorstep.springproject.security.JwtTokenProvider;
import com.doorstep.springproject.services.users.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final GoogleSignInService googleSignInService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider
            , PasswordEncoder passwordEncoder, GoogleSignInService googleSignInService, UserService userService, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.googleSignInService = googleSignInService;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<?> getAccessToken(AuthenticationRequest authenticationRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsernameOrEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        ///
        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.status(200).body(new JwtAuthenticationResponse(jwt, "Bearer"));
    }

    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest){
        if (userService.existsByUserName(signUpRequest.getUserName())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmailAddress(signUpRequest.getEmailAddress()))
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);

        User user = new User(
                signUpRequest.getFullName(),
                signUpRequest.getUserName(),
                signUpRequest.getEmailAddress(),
                signUpRequest.getCountry(),
                signUpRequest.getMobileNumber(),
                signUpRequest.getPassword()
        );

        user.setCreatedAt(new Date().toInstant());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> userRole = roleRepository.findByName(RoleName.ROLE_NORMAL_USER);

        if (!userRole.isPresent()){

            Role role = new Role();
            role.setName(RoleName.ROLE_NORMAL_USER);

            roleRepository.save(role);

            user.setRoles(ImmutableSet.of(role));
        }

        User result = userService.saveUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me/{username}")
                .buildAndExpand(result.getUserName()).toUri();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        result.getEmailAddress(),
                        signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);

        Map<String , Object> response = new HashMap<>();

        response.put("auth",new JwtAuthenticationResponse(jwt, "Bearer"));
        response.put("user",user);

        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<?> processGoogleAuthSignin(GoogleSigninRequest googleSigninRequest) {

        Map<String, Object> response = new LinkedHashMap<>();

        if (googleSigninRequest.getTokenAuthorizationCode().isEmpty()){

            response.put("Status", HttpStatus.BAD_REQUEST);
            response.put("Description","Missing TokenAuthorizationCode");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }

        Optional<GoogleIdToken.Payload> payload = Optional.empty();

        try {
            GoogleIdToken.Payload  payload1 = googleSignInService
                    .verifyToken(String.valueOf(googleSigninRequest.getTokenAuthorizationCode()));

            payload = Optional.of(payload1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        assert payload.isPresent();

        if (!payload.get().getEmailVerified()){

            response.put("status", HttpStatus.NOT_ACCEPTABLE);
            response.put("Description","Email Must Be Verified");

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<User> optionalUser = userService.findByEmailAddress(payload.get().getEmail());

        User authenticatedUser;
        if (!optionalUser.isPresent()){
            User user = new User();
            user.setUserName(String.valueOf(payload.get().get("name")));
            user.setEmailAddress(payload.get().getEmail());
            user.setPictureUrl(String.valueOf(payload.get().get("picture")));
            user.setFullName(String.valueOf(payload.get().get("given_name")) + payload.get().get("family_name"));
            user.setAccountActivated((short) 1);
            Role role = new Role();
            role.setName(RoleName.ROLE_NORMAL_USER);
            user.setRoles(Collections.singleton(role));
            userService.saveUser(user);
            authenticatedUser = user;
        }else
            authenticatedUser = optionalUser.get();

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(authenticatedUser.getUserName()).toUri();

        String jwt = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.created(location).body(new JwtAuthenticationResponse(jwt, "Bearer"));
    }

    public ResponseEntity<?> processAppleAuthSignin(Map<String, Object> appleSigninRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Coming Soon");
    }
}