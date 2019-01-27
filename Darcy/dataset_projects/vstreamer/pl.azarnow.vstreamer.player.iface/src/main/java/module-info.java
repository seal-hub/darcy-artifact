import pl.azarnow.vstreamer.player.iface.PlayerService;

module pl.azarnow.vstreamer.player.iface {

    requires pl.azarnow.vstreamer.movies.iface;
    exports pl.azarnow.vstreamer.player.iface;

    uses PlayerService;

}