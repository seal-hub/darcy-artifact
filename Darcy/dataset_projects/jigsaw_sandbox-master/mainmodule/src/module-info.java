module mainmodule {
    requires java.logging;
    requires customer;
    requires reporting;
    requires hazelcast;
    uses de.synyx.reporting.ReportingFacade;
}