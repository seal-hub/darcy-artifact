package pl.azarnow.vstreamer.movies.core;

import pl.azarnow.vstreamer.movies.iface.Category;
import pl.azarnow.vstreamer.movies.iface.Movie;
import pl.azarnow.vstreamer.movies.iface.MovieService;
import pl.azarnow.vstreamer.people.iface.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository repository;

    MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    public static MovieService provider() {
        return new MovieServiceImpl(new MovieRepository());
    }

    @Override
    public Optional<Movie> find(UUID uuid) {
        return repository.find(uuid);
    }

    @Override
    public List<Movie> findByName(String name) {
        return repository.findByName(name).collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByDirector(Person director) {
        return repository.findByDirector(director).collect(Collectors.toList());
    }

    @Override
    public List<Movie> findByCategory(Category... categories) {
        return repository.findByCategory(categories).collect(Collectors.toList());
    }

    @Override
    public void add(Movie movie) {
        repository.add(movie);
    }
}
