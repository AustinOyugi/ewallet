package com.doorstep.springproject.services.users;

import com.doorstep.springproject.exceptions.ResourceNotFoundException;
import com.doorstep.springproject.models.userdata.User;
import com.doorstep.springproject.payloads.userdata.UserSummary;
import com.doorstep.springproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> getCurrentUser(String userId) {
        return getUserSummary(getUser(userId));
    }

    @Transactional
    public User getUser(String userId){

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (!optionalUser.isPresent())
            throw new ResourceNotFoundException("User", "Id", userId);

        return optionalUser.get();
    }

    public ResponseEntity<?> checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUserName(username);
        Map<String , Object> response = new HashMap<>();
        response.put("IsAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> checkEmailAvailability(String email) {
        Boolean isAvailable = !userRepository.existsByEmailAddress(email);
        Map<String , Object> response = new HashMap<>();
        response.put("IsAvailable", isAvailable);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getUserProfile(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (!optionalUser.isPresent())
            throw new ResourceNotFoundException("User Profile", "Username", username);
        return getUserSummary(optionalUser.get());
    }

    private ResponseEntity<?> getUserSummary(User optionalUser) {
        UserSummary userSummary = new UserSummary();
        userSummary.setUserId(optionalUser.getUserId());
        userSummary.setFullName(optionalUser.getFullName());
        userSummary.setEmailAddress(optionalUser.getEmailAddress());
        userSummary.setPictureUrl(optionalUser.getPictureUrl());
        userSummary.setMobileNumber(optionalUser.getMobileNumber());
        userSummary.setCountry(optionalUser.getCountry());
        userSummary.setAccountActivated(optionalUser.getAccountActivated());
        return ResponseEntity.ok(userSummary);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public boolean existsByEmailAddress(String emailAddress) {
        return userRepository.existsByEmailAddress(emailAddress);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmailAddress(String email) {
        return userRepository.findByEmailAddress(email);
    }
}