package com.interview.ceg.validator;

import com.interview.ceg.dto.Event;
import com.interview.ceg.dto.EventType;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventValidator {

    public Optional<String> validate(EventType eventType, Event event) {
        if (event == null) {
            return Optional.of("Event should not be null");
        }
        if (event.getMetadata() == null) {
            return Optional.of("Event Metadata should not be null");
        }
        if (event.getMetadata().getEventId() == null) {
            return Optional.of("Event Id should not be null");
        }
        if (event.getMetadata().getEventType() == null) {
            return Optional.of("Event Type should not be null");
        }
        if (event.getMetadata().getEventType() != eventType) {
            return Optional.of("Event Types from path and body do not match");
        }
        return Optional.empty();
    }
}
