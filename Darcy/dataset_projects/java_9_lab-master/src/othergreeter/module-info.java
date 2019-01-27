module othergreeter {
    requires greeter;
    exports com.folkol.othergreeter;
    provides com.folkol.greeter.Greeter
        with com.folkol.othergreeter.OtherGreeterImpl;
}

