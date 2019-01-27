package org.modules.customer.domain;

import org.modules.base.domain.Auditable;

import java.time.LocalDate;

/**
 * Created by aleksandra on 05.10.17.
 */
public class Customer extends Auditable {

    private String name;

    private Integer birthYear;

    private boolean isOver18;

    public Customer(String name, Integer birthYear){
        this.name = name;
        this.birthYear = birthYear;
    }

    public boolean isOver18(){
        return isOver18;
    }

    public boolean validate(){
        if(name != null && birthYear != null && isOver18)
            return true;
        else
            return false;
    }

    public boolean validateIsAdult(){
        Integer currentYear = LocalDate.now().getYear();
        if(currentYear - birthYear >= 18)
            return this.isOver18 = true;
        else
            return this.isOver18 = false;
    }
}
