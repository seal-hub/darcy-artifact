package pl.azarnow.vstreamer.app;

import pl.azarnow.vstreamer.movies.iface.Category;
import pl.azarnow.vstreamer.movies.iface.Movie;
import pl.azarnow.vstreamer.movies.iface.MovieDescription;
import pl.azarnow.vstreamer.movies.iface.MovieService;
import pl.azarnow.vstreamer.people.iface.Person;
import pl.azarnow.vstreamer.player.iface.PlayerService;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VStreamerApp {

    public static void main(String[] args) {
        MovieService.getImplementations()
                .findFirst()
                .ifPresent(movieService -> {
                    System.out.println("doing it!");
                    initializeMovies(movieService);
                    PlayerService.getImplementations().findFirst().ifPresent(playerService -> doSth(movieService, playerService));
                });
    }

    private static void initializeMovies(MovieService service) {
        MovieDescription desc = MovieDescription.builder()
                .withName("Blade Runner")
                .withYear(1982)
                .withDirector(new Person("Ridley", "Scott"))
                .withCategories(set(Category.SCI_FI, Category.THRILLER))
                .withDuration(Duration.parse("PT1H57M"))
                .withCast(set(new Person("Harrison", "Ford"), new Person("Rutger", "Hauer")))
                .build();

        service.add(new Movie(UUID.randomUUID(), desc, new byte[]{}));
    }

    private static <T> Set<T> set(T... vals) {
        return Stream.of(vals)
                .collect(Collectors.toSet());
    }

    private static void doSth(MovieService movieService, PlayerService playerService) {
        movieService.findByName("Blade Runner")
                .stream()
                .findFirst()
                .ifPresent(movie -> playerService.play(movie.getId()));
    }

}
