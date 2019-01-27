package pl.azarnow.vstreamer.player.core;

import pl.azarnow.vstreamer.movies.iface.Movie;
import pl.azarnow.vstreamer.movies.iface.MovieService;
import pl.azarnow.vstreamer.player.iface.PlayerService;

import java.util.UUID;

public class PlayerServiceImpl implements PlayerService {

    private final MovieService movieService;

    private PlayerServiceImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    public static PlayerService provider() {
        MovieService movieService = MovieService.getImplementations()
                .findFirst()
                .orElse(null);
        return new PlayerServiceImpl(movieService);
    }


    @Override
    public void play(UUID movieId) {
        movieService.find(movieId).ifPresent(this::play);
    }

    private void play(Movie movie) {
        System.out.println("Playing movie: " + movie.getDescription().getName());
    }
}
