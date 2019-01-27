import pl.azarnow.vstreamer.movies.core.MovieServiceImpl;
import pl.azarnow.vstreamer.movies.iface.MovieService;

module pl.azarnow.vstreamer.movies.core {

    requires pl.azarnow.vstreamer.people.iface;
    requires pl.azarnow.vstreamer.movies.iface;

    provides MovieService with MovieServiceImpl;

}