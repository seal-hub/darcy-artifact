package br.com.casadocodigo.model;

import java.util.List;

public class Book {
    private final String name;
    private final String author;
    private final List<Category> categories;

    public Book(String name, String author, Category...categories) {
        this.name = name;
        this.author = author;
        this.categories = List.of(categories);
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return String.format("\nLivro: %s\nauthor: %s\ncategories: %s", this.name, this.author, this.categories);
    }
}