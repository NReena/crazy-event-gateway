package com.interview.ceg.endpoint;

import com.interview.ceg.dto.Event;
import com.interview.ceg.dto.EventType;
import com.interview.ceg.dto.Problem;
import com.interview.ceg.service.EventRegistrationService;
import com.interview.ceg.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class GatewayController {

    @Autowired
    private EventRegistrationService registrationService;
    @Autowired
    private EventValidator eventValidator;
    @Autowired
    private RateLimiter rateLimiter;

    /**
     *
     * @param eventTypeStr
     * @param event
     * @return
     */
    @PostMapping("/{eventType}/event")
    public ResponseEntity<Problem> postEvent(@PathVariable("eventType") String eventTypeStr, @RequestBody Event event) {
        if (!rateLimiter.isAllowed()) {
            return new ResponseEntity<>(null, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }
        return getEventType(eventTypeStr)
                .map(eventType -> eventValidator.validate(eventType, event)
                                        .map(message -> new ResponseEntity<>(new Problem(message), HttpStatus.BAD_REQUEST))
                                        .orElseGet(() -> {
                                            registrationService.register(event);
                                            return new ResponseEntity<>(null, HttpStatus.CREATED);
                                        })
                )
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    private Optional<EventType> getEventType(String eventTypeStr) {
        try {
            return Optional.of(EventType.valueOf(eventTypeStr));
        } catch (IllegalArgumentException e) {
            log.error("Wrong event type {} received", eventTypeStr);
        }
        return Optional.empty();
    }
}
