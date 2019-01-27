import pl.azarnow.vstreamer.movies.iface.MovieService;

module pl.azarnow.vstreamer.movies.iface {

    requires pl.azarnow.vstreamer.people.iface;

    exports pl.azarnow.vstreamer.movies.iface;

    uses MovieService;
}