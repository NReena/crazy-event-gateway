package com.interview.ceg.processor;

import com.interview.ceg.dto.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ScheduledBatchProcessor {

    @Autowired
    private BatchProcessor processor;

    @Value("${batchprocess.size}")
    private Integer batchSize;

    @Scheduled(fixedRateString = "${batchprocess.timewindow.ms}")
    public void process() {
        log.info("Running scheduler for batch processing");
        Arrays.stream(EventType.values())
                .forEach(eventType -> processor.process(eventType, batchSize));
    }
}
