package com.company;

import com.ms.service.BankService;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Main {

    public static BankService getDefaultProvider(){
        Iterator<BankService> serviceLoader =  ServiceLoader.load(BankService.class).iterator();
        while(serviceLoader.hasNext()){
            System.out.println(serviceLoader.next().getClass());
            return serviceLoader.next();//second one
        }
        throw new RuntimeException("No more implementations found..");
    }

    public static void main(String[] args) {
        double balance = getDefaultProvider().getBalanceWithInterest(5000);
        System.out.println("Total Balance : "+ balance);
    }
}