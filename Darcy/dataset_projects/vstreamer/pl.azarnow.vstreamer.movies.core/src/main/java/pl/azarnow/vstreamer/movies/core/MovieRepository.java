package pl.azarnow.vstreamer.movies.core;

import pl.azarnow.vstreamer.movies.iface.Category;
import pl.azarnow.vstreamer.movies.iface.Movie;
import pl.azarnow.vstreamer.people.iface.Person;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;

class MovieRepository {

    private static final Set<Movie> repo = new HashSet<>();

    void add(Movie movie) {
        repo.add(movie);
    }

    Optional<Movie> find(UUID uuid) {
        return repo.stream()
                .filter(movie -> movie.getId().equals(uuid))
                .findFirst();
    }

    Stream<Movie> findByName(String name) {
        return findWith(movie -> movie.getDescription().getName().equals(name));
    }

    Stream<Movie> findByDirector(Person director) {
        return findWith(movie -> movie.getDescription().getDirector().equals(director));
    }

    Stream<Movie> findByCategory(Category[] categories) {
        return findWith(movie -> containsAny(movie.getDescription().getCategories(), categories));
    }

    private boolean containsAny(Set<Category> existing, Category[] required) {
        return Stream.of(required)
                .anyMatch(existing::contains);
    }

    private Stream<Movie> findWith(Predicate<Movie> pred) {
        return repo.stream().filter(pred);
    }
}
