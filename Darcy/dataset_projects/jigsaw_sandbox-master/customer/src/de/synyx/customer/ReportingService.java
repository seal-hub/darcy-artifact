package de.synyx.customer;

import de.synyx.reporting.ReportingFacade;

public class ReportingService implements ReportingFacade {

    public void report() {
        System.out.println("ich reportiere!");
    }
}
