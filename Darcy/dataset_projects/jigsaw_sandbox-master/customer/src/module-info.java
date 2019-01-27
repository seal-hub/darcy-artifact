module customer {
    requires java.logging;
    exports de.synyx.customer;
    requires reporting;
    provides de.synyx.reporting.ReportingFacade with de.synyx.customer.ReportingService;
}