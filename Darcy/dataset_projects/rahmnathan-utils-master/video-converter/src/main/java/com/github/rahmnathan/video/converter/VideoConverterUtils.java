package com.github.rahmnathan.video.converter;

import com.github.rahmnathan.video.data.SimpleConversionJob;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

class VideoConverterUtils {
    private static final Logger logger = LoggerFactory.getLogger(VideoConverterUtils.class.getName());

    private VideoConverterUtils(){
        // No need to instantiate this
    }

    static void validateParams(SimpleConversionJob simpleConversionJob) {
        Objects.requireNonNull(simpleConversionJob, "Conversion data is null");
        Objects.requireNonNull(simpleConversionJob.getFfmpeg(), "FFmpeg is null");
        Objects.requireNonNull(simpleConversionJob.getFfprobe(), "FFprobe is null");
        Objects.requireNonNull(simpleConversionJob.getInputFile(), "Input file is null");
        Objects.requireNonNull(simpleConversionJob.getOutputFile(), "Output file is null");
    }

    static FFmpegJob buildFFmpegJob(SimpleConversionJob conversionJob) throws IOException {
        String existingFilePath = conversionJob.getInputFile().getAbsolutePath();
        FFmpegProbeResult ffmpegProbeResult = conversionJob.getFfprobe().probe(existingFilePath);

        FFmpegBuilder fFmpegBuilder = createBuilder(conversionJob);
        FFmpegExecutor ffmpegExecutor = new FFmpegExecutor(conversionJob.getFfmpeg(), conversionJob.getFfprobe());

        return ffmpegExecutor.createJob(fFmpegBuilder, progress -> {
            double duration = ffmpegProbeResult.getFormat().duration;
            int percentage = Double.valueOf((progress.out_time_ns / duration) / 10000000).intValue();
            logger.info("{} Encoding progress -> {}%", existingFilePath, percentage);
        });
    }

    static FFmpegBuilder createBuilder(SimpleConversionJob conversionJob) {
        FFmpegOutputBuilder outputBuilder = new FFmpegBuilder()
                .setInput(conversionJob.getInputFile().getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(conversionJob.getOutputFile().getAbsolutePath());

        conversionJob.getContainerFormat().ifPresent(format -> outputBuilder.setFormat(format.name()));
        conversionJob.getAudioCodec().ifPresent(audioCodec -> outputBuilder.setAudioCodec(audioCodec.getEncoder()));
        conversionJob.getVideoCodec().ifPresent(videoCodec -> outputBuilder.setVideoCodec(videoCodec.getEncoder()));

        return outputBuilder.done();
    }

    static FFmpegJob.State waitForResult(FFmpegJob job){
        FFmpegJob.State result = FFmpegJob.State.FAILED;

        while (true) {
            if(job.getState() == FFmpegJob.State.FAILED){
                result = FFmpegJob.State.FAILED;
                break;
            } else if (job.getState() == FFmpegJob.State.FINISHED){
                result = FFmpegJob.State.FINISHED;
                break;
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("Thread sleep operation interrupted", e);
                break;
            }
        }

        return result;
    }
}
