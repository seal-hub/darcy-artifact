package br.com.casadocodigo.logging.impl;

import java.lang.System.Logger;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class CustomLogger implements Logger {
    private final int severity;

    public CustomLogger() {
        this.severity = Level.INFO.getSeverity();
    }

    public CustomLogger(Level level) {
        this.severity = level.getSeverity();
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isLoggable(Level level) {
        return this.severity <= level.getSeverity();
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        if (!this.isLoggable(level)) {
            return;
        }

        System.out.printf("%s [%s]: %s%n", this.getClass().getSimpleName(), level, msg, thrown);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        if (!this.isLoggable(level)) {
            return;
        }

        System.out.printf("%s [%s]: %s%n", this.getClass().getSimpleName(), level, MessageFormat.format(format, params));
    }
}