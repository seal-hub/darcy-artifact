package com.github.rahmnathan.video.control;

import com.github.rahmnathan.video.data.SimpleConversionJob;
import com.github.rahmnathan.video.converter.VideoConverter;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Set;

public class VideoController implements Runnable {
    private final SimpleConversionJob simpleConversionJob;
    private volatile Set<String> activeConversions;
    private final VideoConverter videoConverter = new VideoConverter();
    private final Logger logger = LoggerFactory.getLogger(VideoController.class.getName());

    public VideoController(SimpleConversionJob simpleConversionJob, Set<String> activeConversions) {
        this.simpleConversionJob = simpleConversionJob;
        this.activeConversions = activeConversions;
    }

    @Override
    public void run() {
        MDC.put("Filename", simpleConversionJob.getInputFile().getName());
        String outputFilePath = simpleConversionJob.getOutputFile().getAbsolutePath();
        activeConversions.add(outputFilePath);

        boolean correctFormat = isCorrectFormat(simpleConversionJob);
        logger.info("Correct format? - {}", correctFormat);
        if (!correctFormat) {
            videoConverter.convertMedia(simpleConversionJob);
        }

        activeConversions.remove(outputFilePath);
        MDC.clear();
    }

    private boolean isCorrectFormat(SimpleConversionJob simpleConversionJob) {
        if (simpleConversionJob.getFfprobe() == null) return true;

        boolean correctVideoCodec = !simpleConversionJob.getVideoCodec().isPresent();
        boolean correctAudioCodec = !simpleConversionJob.getAudioCodec().isPresent();
        boolean correctFormat = !simpleConversionJob.getContainerFormat().isPresent();

        try {
            FFmpegProbeResult probeResult = simpleConversionJob.getFfprobe()
                    .probe(simpleConversionJob.getInputFile().getAbsolutePath());

            if(!correctFormat){
                String formatName = probeResult.getFormat().format_name;
                logger.info("Container format - {}", formatName);
                if(formatName != null && formatName.equalsIgnoreCase(simpleConversionJob.getContainerFormat().toString())){
                    correctFormat = true;
                }
            }

            for (FFmpegStream stream : probeResult.getStreams()) {
                String codecName = stream.codec_name;
                logger.info("Stream codec - {}", codecName);
                if (!correctAudioCodec && codecName.equalsIgnoreCase(simpleConversionJob.getAudioCodec().get().name()))
                    correctAudioCodec = true;
                else if (!correctVideoCodec && codecName.equalsIgnoreCase(simpleConversionJob.getVideoCodec().get().name()))
                    correctVideoCodec = true;
            }
        } catch (IOException e) {
            logger.error("Failed to determine video format", e);
        }

        return correctVideoCodec && correctAudioCodec && correctFormat;
    }
}