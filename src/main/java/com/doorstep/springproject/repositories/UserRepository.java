package com.doorstep.springproject.repositories;

import com.doorstep.springproject.models.userdata.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Austin Oyugi
 * @since  3/4/2021
 * @email austinoyugi@gmail.com
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);
    Boolean existsByUserName(String username);
    Boolean existsByEmailAddress(String email);
    Optional<User> findByUserName(String username);
    Optional<User> findByEmailAddress(String email);
    Optional<User> findByUserNameOrEmailAddress(String username, String email);
}