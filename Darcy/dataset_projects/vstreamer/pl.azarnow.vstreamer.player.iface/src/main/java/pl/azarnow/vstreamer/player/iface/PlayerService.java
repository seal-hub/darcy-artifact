package pl.azarnow.vstreamer.player.iface;

import java.util.ServiceLoader;
import java.util.UUID;
import java.util.stream.Stream;

public interface PlayerService {

    static Stream<PlayerService> getImplementations() {
        return ServiceLoader.load(PlayerService.class)
                .stream()
                .map(ServiceLoader.Provider::get);
    }

    void play(UUID movieId);

}
