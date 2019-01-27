package br.com.casadocodigo.nf.service;

import br.com.casadocodigo.nf.model.NF;
import br.com.casadocodigo.tracking.Stack;

public class WSPrefeitura {

    public static void emit(NF nf) {
        try {
            System.out.println("emitindo...");
            Thread.sleep(5000);
            System.out.println("emitido...");
        } catch (Exception e) {
            Stack.logDebugTrace();
            throw new RuntimeException("Falha ao emitir a n", e);
        }
    }
}