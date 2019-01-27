package com.ms.serviceImpl;

import com.ms.service.BankService;

public class BankServiceImpl implements BankService {

    @Override
    public double getBalanceWithInterest(double balance) {
        if(balance < 20000) {
            return balance+ balance * BankService.getDefaultRateOfInterest()/100;
        }
        else {
            return balance + balance * defaultInterest()/100;
        }
    }
}