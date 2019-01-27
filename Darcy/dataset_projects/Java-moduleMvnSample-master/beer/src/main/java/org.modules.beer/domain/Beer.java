package org.modules.beer.domain;

import org.modules.base.domain.Auditable;

/**
 * Created by aleksandra on 05.10.17.
 */
public class Beer extends Auditable {

    private String productName;

    private Integer ibu;

    public Beer(String productName, Integer ibu) {
        this.productName = productName;
        this.ibu = ibu;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getIbu() {
        return ibu;
    }

    public boolean validate(){
        if(this.productName != null && this.ibu != null)
            return true;
        else
            return false;
    }

    @Override
    public String toString(){
        return productName;
    }
}
