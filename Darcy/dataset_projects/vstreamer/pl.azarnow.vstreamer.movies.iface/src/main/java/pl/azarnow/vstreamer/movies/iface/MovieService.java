package pl.azarnow.vstreamer.movies.iface;

import pl.azarnow.vstreamer.people.iface.Person;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.stream.Stream;

public interface MovieService {

    static Stream<MovieService> getImplementations() {
        return ServiceLoader.load(MovieService.class)
                .stream()
                .map(ServiceLoader.Provider::get);
    }

    Optional<Movie> find(UUID uuid);

    List<Movie> findByName(String name);

    List<Movie> findByDirector(Person director);

    List<Movie> findByCategory(Category... categories);

    void add(Movie movie);

}
