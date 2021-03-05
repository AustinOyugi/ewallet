package com.doorstep.springproject.controller;

import com.doorstep.springproject.services.payments.WalletService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Austin Oyugi
 * @since  3/5/2021
 * @email austinoyugi@gmail.com
 */

@RestController
@RequestMapping("/api/v1/wallet/")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @ApiOperation("Add Wallet to user")
    @GetMapping("addnew/{userid}")
    private ResponseEntity<?> addNewUserWallet(@ApiParam(name = "userid") @PathVariable String userid){
        return walletService.addNewUserWallet(userid);
    }

    @ApiOperation("List all User Wallets")
    @GetMapping("listall/{userid}")
    private ResponseEntity<?> listAllWallets(@ApiParam(name = "userid") @PathVariable String userid){
        return walletService.listAllUserWallets(userid);
    }
}
