/**
 * Created by sbulavin on 20-Nov-2017.
 */
module jwtgen.generator {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.beans;
    requires validation.api;
    requires java.jwt;
    requires spring.context;
    requires bcprov.jdk15on;
    requires bcpkix.jdk15on;
    exports com.optus.jwtgen.controller to com.testjava;

}