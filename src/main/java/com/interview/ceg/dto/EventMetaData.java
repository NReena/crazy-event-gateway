package com.interview.ceg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class EventMetaData {

    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("event_type")
    private EventType eventType;
    @JsonProperty("occurred_at")
    private OffsetDateTime occurredAt;
}
