package com.ms.service;

public interface BankService {

    private double savingBankRateOfInterest(){
        return BankService.getDefaultRateOfInterest()+2.4; // Some calculations
    }

    default double defaultInterest(){
        System.out.println("Saving Interest Rate from -> Interface private method : "+ savingBankRateOfInterest());
        return savingBankRateOfInterest();
    }

    static double getDefaultRateOfInterest(){
        return 3.1;
    }

    public double getBalanceWithInterest(double balance);

}