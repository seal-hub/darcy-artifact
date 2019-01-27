package de.synyx.main;

import com.hazelcast.core.HazelcastInstance;    
import de.synyx.customer.CustomerService;
import de.synyx.reporting.ReportingFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;       
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger("MainLogger");

    public static void main(String[] args) {
	    LOG.info("Moinmoin");
        CustomerService customerService = new CustomerService();
        customerService.halloCustomer();

        List<ReportingFacade> reportingServices = new ArrayList<>();
       
        ServiceLoader.load(ReportingFacade.class).forEach(reportingServices::add);

        reportingServices.get(0).report();
        HazelcastInstance hazelcastInstance = null;
    }
}
