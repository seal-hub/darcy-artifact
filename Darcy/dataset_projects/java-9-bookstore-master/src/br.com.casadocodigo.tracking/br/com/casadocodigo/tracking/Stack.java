package br.com.casadocodigo.tracking;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.stream.Collectors;

public class Stack {

    public static void logDebugTrace() {
        Logger logger = System.getLogger("CustomLogger");

        StackWalker.getInstance()
            .walk(stream -> {
                return stream.skip(1)
                    .peek(element -> {
                        logger.log(
                            Level.TRACE,
                            String.format("linha %s do método %s da classe %s", element.getLineNumber(), element.getMethodName(), element.getClassName())
                        );
                    })
                    .collect(Collectors.toList());
            });
    }
}