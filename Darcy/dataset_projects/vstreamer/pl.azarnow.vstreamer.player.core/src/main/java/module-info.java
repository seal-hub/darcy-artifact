import pl.azarnow.vstreamer.movies.iface.MovieService;
import pl.azarnow.vstreamer.player.core.PlayerServiceImpl;
import pl.azarnow.vstreamer.player.iface.PlayerService;

module pl.azarnow.vstreamer.player.core {

    requires pl.azarnow.vstreamer.movies.iface;
    requires pl.azarnow.vstreamer.player.iface;

    uses MovieService;
    provides PlayerService with PlayerServiceImpl;

}