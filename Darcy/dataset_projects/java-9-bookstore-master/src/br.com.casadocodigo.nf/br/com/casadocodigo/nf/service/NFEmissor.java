package br.com.casadocodigo.nf.service;

import java.util.concurrent.SubmissionPublisher;

import br.com.casadocodigo.model.Book;
import br.com.casadocodigo.nf.model.NF;

public class NFEmissor {
    private SubmissionPublisher<NF> publisher;

    public NFEmissor() {
        this.publisher = new SubmissionPublisher<>();
        publisher.subscribe(new NFSubscriber());
    }

    public void emit(String clientName, Book book) {
        NF nf = new NF(clientName, book.getName(), 39.99);
        this.publisher.submit(nf);
    }

    public void close() {
        this.publisher.close();
    }
}