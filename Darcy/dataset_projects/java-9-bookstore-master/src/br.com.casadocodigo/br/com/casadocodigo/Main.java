package br.com.casadocodigo;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import br.com.casadocodigo.http.Books;
import br.com.casadocodigo.model.Book;
import br.com.casadocodigo.nf.service.NFEmissor;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class Main {
    private static Consumer<Book> showSimilar = similar -> System.out.println(String.format("\nTalvez você também goste do livro: %s", similar.getName()));
    private static Runnable noSuggestions = () -> System.out.println("\nNão temos nenhuma sugestão de livro similar no momento");

    public static void main(String[] args) {
        Logger logger = System.getLogger("CustomLogger");

        logger.log(Level.TRACE, "Iniciando a execução da bookstore");

        System.out.println("\nLista de livros disponíveis \n");

        List<Book> books = Books.all();

        IntStream.range(0, books.size())
            .forEach(i -> System.out.println(i + " - " + books.get(i).getName()));

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDigite o número do livro que quer comprar: ");

        try {
            int number = scanner.nextInt();
            Book book = books.get(number);

            logger.log(Level.INFO, "\nO livro escolhido foi: " + book.getName());
            System.out.println("\nInforme seu nome, para que possamos emitir a nota fiscal: ");

            scanner = new Scanner(System.in);
            String name = scanner.nextLine();

            NFEmissor emissor = new NFEmissor();
            emissor.emit(name, book);

            System.out.println("\nObrigado!");

            Books.findSimilar(book)
                .ifPresentOrElse(showSimilar, noSuggestions);

            System.out.println("\nAperte o enter para sair");
            new Scanner(System.in).nextLine();

            emissor.close();
        } catch(Exception e) {
            logger.log(Level.ERROR, "Ops, aconteceu um erro" + e.getMessage(), e);
            throw new RuntimeException("Ops, aconteceu um erro", e);
        }

        logger.log(Level.TRACE, "Terminando a execução da bookstore");
    }
}