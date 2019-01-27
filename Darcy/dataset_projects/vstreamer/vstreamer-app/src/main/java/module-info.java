import pl.azarnow.vstreamer.movies.iface.MovieService;
import pl.azarnow.vstreamer.player.iface.PlayerService;

module pl.azarnow.vstreamer.app {

    requires pl.azarnow.vstreamer.movies.iface;
    requires pl.azarnow.vstreamer.player.iface;
    requires pl.azarnow.vstreamer.people.iface;

    uses MovieService;
    uses PlayerService;

}