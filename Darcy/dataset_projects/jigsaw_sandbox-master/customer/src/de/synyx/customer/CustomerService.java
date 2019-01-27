package de.synyx.customer;

//import java.util.logging.Logger;


import java.util.logging.Logger;

public class CustomerService implements CustomerServiceFacade {

    private static Logger LOG = Logger.getLogger("CustomerService");

    @Override
    public void halloCustomer() {
        LOG.info("hallo, ich bin der CustomerService");
    }
}
