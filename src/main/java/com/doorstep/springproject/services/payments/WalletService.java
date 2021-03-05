package com.doorstep.springproject.services.payments;

import com.doorstep.springproject.exceptions.ResourceNotFoundException;
import com.doorstep.springproject.models.payments.Wallet;
import com.doorstep.springproject.models.userdata.User;
import com.doorstep.springproject.repositories.WalletRepository;
import com.doorstep.springproject.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Austin Oyugi
 * @since  3/5/2021
 * @email austinoyugi@gmail.com
 */

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserService userService;

    @Autowired
    public WalletService(WalletRepository walletRepository, UserService userService) {
        this.walletRepository = walletRepository;
        this.userService = userService;
    }

    public ResponseEntity<?> addNewUserWallet(String userid) {
        User optionalUser =  userService.getUser(userid);

        if (Objects.isNull(optionalUser)){
            throw new ResourceNotFoundException("User","UserId",userid);
        }

        String generatedAccountNumber = generateRandomAccountNumber();

        while (walletRepository.existsByAccountNumber(generatedAccountNumber)){
            generatedAccountNumber = generateRandomAccountNumber();
        }

        Wallet wallet = new Wallet(optionalUser.getUserName(),generatedAccountNumber,optionalUser);

        return ResponseEntity.ok(walletRepository.save(wallet));
    }

    private String generateRandomAccountNumber(){

        Random random = new Random();

        char[] digits = new char[12];

        digits[0] = (char) (random.nextInt(9) + '1');

        for (int i = 1; i<12; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }

        return  new String(digits);
    }

    public ResponseEntity<?> listAllUserWallets(String userid) {

        User optionalUser =  userService.getUser(userid);

        if (Objects.isNull(optionalUser)){
            throw new ResourceNotFoundException("User","UserId",userid);
        }

        return ResponseEntity.ok(walletRepository.findAllByWalletuser(optionalUser));
    }
}
