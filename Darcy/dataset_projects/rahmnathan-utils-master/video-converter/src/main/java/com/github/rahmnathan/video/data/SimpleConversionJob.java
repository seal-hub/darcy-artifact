package com.github.rahmnathan.video.data;

import com.github.rahmnathan.video.codec.AudioCodec;
import com.github.rahmnathan.video.codec.ContainerFormat;
import com.github.rahmnathan.video.codec.VideoCodec;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;

import java.io.File;
import java.util.Optional;

public class SimpleConversionJob {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final File inputFile;
    private final File outputFile;
    private final ContainerFormat containerFormat;
    private final AudioCodec audioCodec;
    private final VideoCodec videoCodec;

    private SimpleConversionJob(File inputFile, File outputFile, FFmpeg ffmpeg, FFprobe ffprobe,
                                AudioCodec audioCodec, VideoCodec videoCodec, ContainerFormat containerFormat) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.ffmpeg = ffmpeg;
        this.ffprobe = ffprobe;
        this.containerFormat = containerFormat;
        this.audioCodec = audioCodec;
        this.videoCodec = videoCodec;
    }

    public Optional<ContainerFormat> getContainerFormat() {
        return Optional.ofNullable(containerFormat);
    }

    public Optional<AudioCodec> getAudioCodec() {
        return Optional.ofNullable(audioCodec);
    }

    public Optional<VideoCodec> getVideoCodec() {
        return Optional.ofNullable(videoCodec);
    }

    public FFmpeg getFfmpeg() {
        return ffmpeg;
    }

    public FFprobe getFfprobe() {
        return ffprobe;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public File getInputFile() {
        return inputFile;
    }

    public static class Builder {
        private File inputFile;
        private File outputFile;
        private FFmpeg ffmpeg;
        private FFprobe ffprobe;
        private ContainerFormat containerFormat;
        private VideoCodec videoCodec;
        private AudioCodec audioCodec;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder setContainerFormat(ContainerFormat containerFormat) {
            this.containerFormat = containerFormat;
            return this;
        }

        public Builder setVideoCodec(VideoCodec videoCodec) {
            this.videoCodec = videoCodec;
            return this;
        }

        public Builder setAudioCodec(AudioCodec audioCodec) {
            this.audioCodec = audioCodec;
            return this;
        }

        public Builder setFfmpeg(FFmpeg ffmpeg) {
            this.ffmpeg = ffmpeg;
            return this;
        }

        public Builder setFfprobe(FFprobe ffprobe) {
            this.ffprobe = ffprobe;
            return this;
        }

        public Builder setOutputFile(File outputFile) {
            this.outputFile = outputFile;
            return this;
        }

        public Builder setInputFile(File inputFile) {
            this.inputFile = inputFile;
            return this;
        }

        public SimpleConversionJob build() {
            return new SimpleConversionJob(inputFile, outputFile, ffmpeg, ffprobe, audioCodec, videoCodec, containerFormat);
        }
    }
}