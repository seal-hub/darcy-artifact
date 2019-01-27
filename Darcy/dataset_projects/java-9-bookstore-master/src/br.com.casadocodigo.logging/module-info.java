module br.com.casadocodigo.logging {
    provides
        java.lang.System.LoggerFinder
    with
        br.com.casadocodigo.logging.impl.CustomLoggerFinder;
}