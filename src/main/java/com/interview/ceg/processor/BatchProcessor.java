package com.interview.ceg.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.ceg.dto.Event;
import com.interview.ceg.dto.EventType;
import com.interview.ceg.store.EventStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class BatchProcessor {

    @Autowired
    private EventStore eventStore;

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${batchprocess.eventlogdir}")
    private String directory;

    @PostConstruct
    public void init() {
        Arrays.stream(EventType.values())
                .forEach(eventType -> createFileIfNotExists(logFileName(eventType)));
    }

    private void createFileIfNotExists(String logFileName) {
        try {
            Path dir = Paths.get(directory);
            if (Files.notExists(dir)){
                Files.createDirectory(dir);
            }
            Path file = Paths.get(logFileName);
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create log file", e);
        }
    }

    private String logFileName(EventType eventType) {
        return directory + "/" + eventType.name() + ".log";
    }

    @Async
    public void process(EventType eventType, int batchSize) {
        log.info("Processing batch for Event Type: {} and batch size: {}", eventType, batchSize);
        try {
            writeToFile(logFileName(eventType), () -> {
                List<Event> events = eventStore.remove(eventType, batchSize);
                StringBuilder builder = new StringBuilder();
                String content =  events.stream()
                        .map(this::toString)
                        .reduce("", (s1, s2) -> s1 + "\n" + s2);
                return (content.length() > 1) ? content.substring(1) : content;
            });
        } catch (IOException e) {
            log.error("Error writing events to the file name", e);
        }
    }

    private String toString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize " + o.getClass(), e);
        }
    }

    private void writeToFile(String fileName, Supplier<String> contentSupplier) throws IOException {
        //Get the file reference
        Path path = Paths.get(fileName);
        String content = contentSupplier.get();
        log.info("Writing to file {}: {}", fileName, content);
        if (!StringUtils.isEmpty(content)) {
            content = content + "\n";
            Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
        }
    }
}
