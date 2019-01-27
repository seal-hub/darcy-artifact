package com.github.rahmnathan.video.codec;

public enum AudioCodec {
    AAC("aac");

    private final String ffmpegFormat;

    AudioCodec(String ffmpegFormat) {
        this.ffmpegFormat = ffmpegFormat;
    }

    public String getEncoder() {
        return ffmpegFormat;
    }
}
