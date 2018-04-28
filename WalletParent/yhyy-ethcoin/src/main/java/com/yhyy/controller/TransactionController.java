package com.yhyy.controller;

import com.yhyy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

@RestController
@CrossOrigin
@RequestMapping("/eth/trans")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/sendTransaction")
    public EthSendTransaction sendTransaction(@RequestParam(value = "from", required = true) String from,
                                              @RequestParam(value = "fromPassword", required = true) String fromPassword,
                                              @RequestParam(value = "to", required = true) String to,
                                              @RequestParam(value = "amount", required = true) long amount) {
        return transactionService.sendTransaction(from, fromPassword, to, amount);
    }

}
