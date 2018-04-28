package com.yhyy.controller;

import com.yhyy.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;

@RestController
@CrossOrigin
@RequestMapping("/eth/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/createAccount")
    public NewAccountIdentifier createAccount(@RequestParam(value = "password", required = true) String password) {
        return accountService.createAccount(password);
    }
}
