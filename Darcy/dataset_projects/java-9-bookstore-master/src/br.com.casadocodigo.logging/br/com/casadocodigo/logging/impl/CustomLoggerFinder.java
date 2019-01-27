package br.com.casadocodigo.logging.impl;

import java.lang.System.Logger;
import java.lang.System.LoggerFinder;
import java.lang.System.Logger.Level;

import br.com.casadocodigo.logging.impl.CustomLogger;

public class CustomLoggerFinder extends LoggerFinder {

    @Override
    public Logger getLogger(String name, Module module) {
        System.out.printf("[%s]: [name=%s, module=%s]\n", this.getClass().getSimpleName(), name, module);
        
        return new CustomLogger(Level.ALL);
    }
}