package md.utm.fcim;

public class Gateway {

    public static void main(String[] args) {
        ManagementGateway managementGateway = new ManagementGateway(5555);
        managementGateway.start();
    }
}
