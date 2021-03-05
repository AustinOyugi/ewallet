package com.doorstep.springproject.repositories;

import com.doorstep.springproject.models.payments.Wallet;
import com.doorstep.springproject.models.userdata.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Austin Oyugi
 * @since  3/5/2021
 * @email austinoyugi@gmail.com
 */

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    boolean existsByAccountNumber(String accNumber);

    List<Wallet> findAllByWalletuser(User user);
}
