module video.converter {
    exports com.github.rahmnathan.video.data;
    exports com.github.rahmnathan.video.codec;
    exports com.github.rahmnathan.video.control;
    requires ffmpeg;
    requires slf4j.api;
}