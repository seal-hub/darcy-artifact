package org.modules.pub.service;

import org.modules.beer.dao.BeerDao;
import org.modules.beer.domain.Beer;
import org.modules.customer.dao.CustomerDao;
import org.modules.customer.domain.Customer;

import java.util.Map;

/**
 * Created by aleksandra on 06.10.17.
 */
public class BartenderImpl implements Bartender{

    CustomerDao customerDao = new CustomerDao();
    BeerDao beerDao = new BeerDao();

    @Override
    public void sayHello() {
        System.out.println("Welcome in the pub.");
        System.out.println("What's your name?");
        String name = System.console().readLine();
        System.out.println(String.format("Hi %s, you have to forgive me, but I have to check your ID.", name));
        checkID(name);
    }

    @Override
    public void checkID(String name) {
        System.out.println("Give year of your birth.");
        String answear = System.console().readLine();
        Integer year = Integer.valueOf(answear);
        Customer customer = new Customer(name, year);
        boolean isAdult = customer.validateIsAdult();

        if(isAdult) {
            customerDao.add(customer);
            System.out.println("Alright, choose what you want");
            suggestBeer();
        }
        else
            throwOutOfPub();

    }

    @Override
    public void throwOutOfPub() {
        System.out.println("Sorry, but you can not drink alcohol yet. Please get out.");
    }

    @Override
    public void suggestBeer() {
        Map<Long, Beer> beers = beerDao.getAll();

        for(Long index : beers.keySet()){
            System.out.println(index.toString() + " - " + beers.get(index).toString());
        }
        String answear = System.console().readLine();
        Long beerId = Long.valueOf(answear);

        Beer customerChoice = beerDao.getOne(beerId);

        if(customerChoice != null){
            System.out.println("Here you go. Enjoy the beer!");
            sayGoodBay();
        } else {
            System.out.println("Sorry, we don't have it. Try to choose once again");
            suggestBeer();
        }
    }

    @Override
    public void sayGoodBay() {
        System.out.println("Bye, bye! See you soon.");
    }
}
