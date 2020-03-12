package com.interview.ceg.service;

import com.interview.ceg.dto.Event;
import com.interview.ceg.processor.BatchProcessor;
import com.interview.ceg.store.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventRegistrationService {

    @Autowired
    private EventStore eventStore;

    @Value("${batchprocess.size}")
    private Integer batchSize;

    private BatchProcessor batchProcessor;

    public void register(Event event) {
        int count = eventStore.store(event);
        if (count >= batchSize) {
            // process batch in async
            batchProcessor.process(event.getMetadata().getEventType(), batchSize);
        }
    }
}
