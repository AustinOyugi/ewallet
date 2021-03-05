package com.doorstep.springproject.services.auth;

import com.doorstep.springproject.exceptions.ResourceNotFoundException;
import com.doorstep.springproject.models.userdata.User;
import com.doorstep.springproject.repositories.UserRepository;
import com.doorstep.springproject.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameOrEmailAddress(usernameOrEmail, usernameOrEmail)
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found with username or email :" + usernameOrEmail));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id)
    {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw  new ResourceNotFoundException("User", "id", id);
        }
        return UserPrincipal.create(user.get());
    }
}
