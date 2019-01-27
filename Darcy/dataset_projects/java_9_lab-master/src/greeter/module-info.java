module greeter {
    exports com.folkol.greeter;
    provides com.folkol.greeter.Greeter
        with com.folkol.greeter.GreeterImpl;
}

