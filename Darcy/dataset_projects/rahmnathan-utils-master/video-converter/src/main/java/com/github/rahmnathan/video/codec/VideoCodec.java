package com.github.rahmnathan.video.codec;

public enum VideoCodec {
    H264("libx264");

    private final String ffmpegFormat;

    VideoCodec(String ffmpegFormat) {
        this.ffmpegFormat = ffmpegFormat;
    }

    public String getEncoder() {
        return ffmpegFormat;
    }
}
